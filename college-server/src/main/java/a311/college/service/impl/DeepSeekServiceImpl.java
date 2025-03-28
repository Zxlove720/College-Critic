package a311.college.service.impl;

import a311.college.constant.deepseek.DeepSeekConstant;
import a311.college.constant.redis.DeepSeekRedisKey;
import a311.college.entity.ai.UserAIRequestMessage;
import a311.college.entity.ai.UserAIRequest;
import a311.college.service.DeepSeekService;
import a311.college.thread.ThreadLocalUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * DeepSeek多轮对话
 */
@Slf4j
@Service
public class DeepSeekServiceImpl implements DeepSeekService {

    // 通过Redis存储用户对话历史
    private final RedisTemplate<String, Object> redisTemplate;

    public DeepSeekServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 请求DeepSeekAPI并获取响应
     *
     * @param request 请求
     * @return UserAIRequestMessage 将DeepSeek的响应封装为Message对象返回
     */
    @Override
    public UserAIRequestMessage response(UserAIRequest request) {
        // 1.初始化用户信息
        initUserMessageHistory();
        try {
            // 2.将用户的问题加入历史消息记录，存入Redis
            addMessage(request.getMessage());
        } catch (RedisConnectionFailureException e) {
            log.error("redis连接异常");
        }
        // 3.获取用户对话的历史消息
        JSONArray messages = buildHistoryMessageArray();
        // 4.构建请求
        OkHttpClient client = new OkHttpClient.Builder()
                // 4.1设置连接超时时间
                .connectTimeout(60, TimeUnit.SECONDS)
                // 4.2设置读取超时时间
                .readTimeout(60, TimeUnit.SECONDS)
                // 4.3设置写入超时时间
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        // 构建请求体
        JSONObject requestBody = new JSONObject();
        // 选择模型
        requestBody.put("model", DeepSeekConstant.MODEL_NAME);
        // 构建消息
        requestBody.put("messages", messages);
        // 是否开启流式输出
        requestBody.put("stream", request.getStream());
        // 请求DeepSeek
        Request seekRequest = new Request.Builder()
                .url(DeepSeekConstant.API_URL)
                .addHeader("Authorization", "Bearer " + DeepSeekConstant.API_KEY)
                .addHeader("Content-Type", DeepSeekConstant.PARSE_SET)
                .post(RequestBody.create(
                        requestBody.toJSONString(),
                        MediaType.parse(DeepSeekConstant.PARSE_SET)))
                .build();
        // 获取响应
        try (Response response = client.newCall(seekRequest).execute()) {
            if (!response.isSuccessful()) {
                log.info(DeepSeekConstant.ERROR_CONSTANT);
                return new UserAIRequestMessage(DeepSeekConstant.ROLE_ASSISTANT, "error");
            }
            JSONObject responseJson = null;
            // 获取响应体
            if (response.body() != null) {
                responseJson = JSON.parseObject(response.body().string());
            }
            // 封装答案
            String answer = null;
            if (responseJson != null) {
                answer = extractAnswer(responseJson);
            }
            // 添加助手回复到历史
            addAssistantMessage(answer);
            log.info(DeepSeekConstant.ROLE_ASSISTANT + "{}", answer);
            // 返回回答
            return new UserAIRequestMessage(DeepSeekConstant.ROLE_ASSISTANT, answer);
        } catch (IOException e) {
            log.info(DeepSeekConstant.REQUEST_CONSTANT);
        }
        return new UserAIRequestMessage(DeepSeekConstant.ROLE_ASSISTANT, "error");
    }


    /**
     * 封装不同用户的历史消息Key
     *
     * @return 不同用户的消息Key
     */
    private String buildMessageKey() {
        return DeepSeekRedisKey.DEEP_SEEK_HISTORY_KEY + ThreadLocalUtil.getCurrentId();
    }

    /**
     * 初始化用户信息
     */
    private void initUserMessageHistory() {
        // 1.获取不同用户的Key
        String key = buildMessageKey();
        // 2.判断用户的Key是否存在（是否需要初始化）
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            // 2.1Key不存在，封装初始化信息并存入Redis
            JSONObject systemMessage = new JSONObject()
                    .fluentPut("role", DeepSeekConstant.ROLE_SYSTEM)
                    .fluentPut("content", DeepSeekConstant.INIT_CONSTANT);
            redisTemplate.opsForList().rightPush(key, systemMessage.toJSONString());
            redisTemplate.expire(key, DeepSeekRedisKey.DEEP_SEEK_HISTORY_TTL, TimeUnit.HOURS);
        }
    }

    /**
     * 添加用户对话消息
     *
     * @param userAIRequestMessage 用户对话消息对象
     */
    private void addMessage(UserAIRequestMessage userAIRequestMessage) {
        // 1.如果用户消息为空，则报错
        if (userAIRequestMessage == null) {
            throw new IllegalArgumentException("message is null");
        }
        // 2.将用户的消息加入redis
        JSONObject message = new JSONObject()
                .fluentPut("role", userAIRequestMessage.getRole())
                .fluentPut("content", userAIRequestMessage.getContent());
        redisTemplate.opsForList().rightPush(buildMessageKey(), message.toJSONString());
    }

    /**
     * 构造历史消息数组
     *
     * @return JSONArray 历史消息数组
     */
    private JSONArray buildHistoryMessageArray() {
        // 将用户的历史消息构造为JSONArray数组
        List<Object> messages = redisTemplate.opsForList().range(buildMessageKey(), 0, -1);
        return Optional.ofNullable(messages).map(list -> list.stream()
                        .map(String::valueOf)
                        .map(JSON::parseObject)
                        .collect(Collectors.toCollection(JSONArray::new)))
                .orElseGet(JSONArray::new);
    }

    /**
     * 添加助手消息到历史记录（新增方法）
     */
    private void addAssistantMessage(String content) {
        UserAIRequestMessage message = new UserAIRequestMessage(
                DeepSeekConstant.ROLE_ASSISTANT,
                Optional.ofNullable(content).orElse("")
        );
        addMessage(message); // 复用已有添加逻辑
    }

    /**
     * 从响应中提取答案（新增方法）
     */
    private String extractAnswer(JSONObject responseJson) {
        try {
            return responseJson.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
        } catch (Exception e) {
            log.error("解析响应异常: {}", e.getMessage());
            return "答案解析失败";
        }
    }

}


