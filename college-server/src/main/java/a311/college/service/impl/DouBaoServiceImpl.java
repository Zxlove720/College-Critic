package a311.college.service.impl;

import a311.college.constant.redis.DouBaoRedisKey;
import a311.college.dto.ai.UserAIRequestDTO;
import a311.college.exception.DouBaoAPIErrorException;
import a311.college.service.DouBaoService;
import a311.college.thread.ThreadLocalUtil;
import a311.college.vo.ai.UserAIMessageVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DouBaoServiceImpl implements DouBaoService {

    // 豆包API配置常量（需在常量类中补充）
    private static final String API_URL = "https://ark.cn-beijing.volces.com/api/v3/chat/completions";
    private static final String API_KEY = "your-doubao-api-key";
    private static final String MODEL_NAME = "doubao-1-5-pro-256k-250115";
    private static final String ROLE_SYSTEM = "system";
    private static final String ROLE_ASSISTANT = "assistant";
    private static final String INIT_CONSTANT = "你是一个专业的AI助手";

    private final RedisTemplate<String, Object> redisTemplate;

    public DouBaoServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public UserAIMessageVO response(UserAIRequestDTO request) {
        initUserMessageHistory();
        try {
            addMessage(request.getMessage());
        } catch (Exception e) {
            log.error("Redis连接异常", e);
        }

        JSONArray messages = buildHistoryMessageArray();

        // 豆包API请求体构建（参考网页3）
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL_NAME);
        requestBody.put("messages", messages);
        requestBody.put("temperature", request.getTemperature());
        requestBody.put("stream", false);

        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

        Request seekRequest = new Request.Builder()
            .url(API_URL)
            .addHeader("Authorization", "Bearer " + API_KEY)
            .addHeader("Content-Type", "application/json")
            .post(RequestBody.create(
                requestBody.toJSONString(),
                MediaType.parse("application/json")))
            .build();

        try (Response response = client.newCall(seekRequest).execute()) {
            if (!response.isSuccessful()) {
                log.error("API请求失败，状态码：{}", response.code());
                return errorResponse();
            }

            JSONObject responseJson = null;
            if (response.body() != null) {
                responseJson = JSON.parseObject(response.body().string());
            }
            String answer = extractAnswer(responseJson);

            addAssistantMessage(answer);
            return new UserAIMessageVO(ROLE_ASSISTANT, answer);
        } catch (IOException e) {
            log.error("API调用异常", e);
            throw new DouBaoAPIErrorException("豆包服务调用失败");
        }
    }

    // Redis相关方法与原有结构保持一致
    private String buildUserMessageKey() {
        return DouBaoRedisKey.DOUBAO_HISTORY_KEY + ThreadLocalUtil.getCurrentId();
    }

    private void initUserMessageHistory() {
        String key = buildUserMessageKey();
        if (!redisTemplate.hasKey(key)) {
            JSONObject systemMessage = new JSONObject()
                .fluentPut("role", ROLE_SYSTEM)
                .fluentPut("content", INIT_CONSTANT);
            redisTemplate.opsForList().rightPush(key, systemMessage.toJSONString());
            redisTemplate.expire(key, 24, TimeUnit.HOURS); // 对话历史保留24小时
        }
    }

    private void addMessage(UserAIMessageVO message) {
        JSONObject msg = new JSONObject()
            .fluentPut("role", message.getRole())
            .fluentPut("content", message.getContent());
        redisTemplate.opsForList().rightPush(buildUserMessageKey(), msg.toJSONString());
    }

    private JSONArray buildHistoryMessageArray() {
        List<Object> messages = redisTemplate.opsForList().range(buildUserMessageKey(), 0, -1);
        return Optional.ofNullable(messages)
            .map(list -> list.stream()
                .map(String::valueOf)
                .map(JSON::parseObject)
                .collect(Collectors.toCollection(JSONArray::new)))
            .orElseGet(JSONArray::new);
    }

    private void addAssistantMessage(String content) {
        addMessage(new UserAIMessageVO(ROLE_ASSISTANT, content));
    }

    private String extractAnswer(JSONObject response) {
        try {
            return response.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
        } catch (Exception e) {
            log.error("响应解析异常", e);
            return "服务响应解析失败";
        }
    }

    private UserAIMessageVO errorResponse() {
        return new UserAIMessageVO(ROLE_ASSISTANT, "当前服务不可用，请稍后重试");
    }
}