package a311.college.service.impl;

import a311.college.constant.deepseek.DeepSeekConstant;
import a311.college.constant.redis.DeepSeekRedisKey;
import a311.college.dto.ai.MajorAIRequestDTO;
import a311.college.dto.ai.SchoolAIRequestDTO;
import a311.college.exception.DeepSeekAPIErrorException;
import a311.college.mapper.major.MajorMapper;
import a311.college.mapper.school.SchoolMapper;
import a311.college.vo.ai.MajorAIMessageVO;
import a311.college.vo.ai.SchoolAIMessageVO;
import a311.college.vo.ai.UserAIMessageVO;
import a311.college.dto.ai.UserAIRequestDTO;
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

    private final SchoolMapper schoolMapper;

    private final MajorMapper majorMapper;

    public DeepSeekServiceImpl(RedisTemplate<String, Object> redisTemplate, SchoolMapper schoolMapper, MajorMapper majorMapper) {
        this.redisTemplate = redisTemplate;
        this.schoolMapper = schoolMapper;
        this.majorMapper = majorMapper;
    }

    /**
     * 请求DeepSeekAPI并获取响应
     *
     * @param request 请求
     * @return UserAIMessageVO 将DeepSeek的响应封装为Message对象返回
     */
    @Override
    public UserAIMessageVO response(UserAIRequestDTO request) {
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
        // 4.4构建请求体
        JSONObject requestBody = new JSONObject();
        // 4.5选择模型
        requestBody.put("model", DeepSeekConstant.MODEL_NAME);
        // 4.6构建消息
        requestBody.put("messages", messages);
        // 4.7是否开启流式输出
        requestBody.put("stream", false);
        // 4.8发起请求，请求DeepSeek
        Request seekRequest = new Request.Builder()
                .url(DeepSeekConstant.API_URL)
                .addHeader("Authorization", "Bearer " + DeepSeekConstant.API_KEY)
                .addHeader("Content-Type", DeepSeekConstant.PARSE_SET)
                .post(RequestBody.create(
                        requestBody.toJSONString(),
                        MediaType.parse(DeepSeekConstant.PARSE_SET)))
                .build();
        // 5.获取响应
        try (Response response = client.newCall(seekRequest).execute()) {
            if (!response.isSuccessful()) {
                log.info(DeepSeekConstant.RESPONSE_ERROR_CONSTANT);
                return new UserAIMessageVO(DeepSeekConstant.ROLE_ASSISTANT, "error");
            }
            JSONObject responseJson = null;
            // 5.1获取响应体
            if (response.body() != null) {
                responseJson = JSON.parseObject(response.body().string());
            }
            // 5.2封装答案
            String answer = null;
            if (responseJson != null) {
                answer = extractAnswer(responseJson);
            }
            // 5.3添加助手回复到历史
            addAssistantMessage(answer);
            log.info(DeepSeekConstant.ROLE_ASSISTANT + "{}", answer);
            // 5.4返回回答
            return new UserAIMessageVO(DeepSeekConstant.ROLE_ASSISTANT, answer);
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new DeepSeekAPIErrorException(DeepSeekConstant.RESPONSE_ERROR_CONSTANT);
        }
    }

    /**
     * 封装不同用户的历史消息Key
     *
     * @return 不同用户的消息Key
     */
    private String buildUserMessageKey() {
        return DeepSeekRedisKey.DEEP_SEEK_HISTORY_KEY + ThreadLocalUtil.getCurrentId();
    }

    /**
     * 初始化用户信息
     */
    private void initUserMessageHistory() {
        // 1.获取不同用户的Key
        String key = buildUserMessageKey();
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
     * @param userAIMessageVO 用户对话消息对象
     */
    private void addMessage(UserAIMessageVO userAIMessageVO) {
        // 1.如果用户消息为空，则报错
        if (userAIMessageVO == null) {
            throw new IllegalArgumentException("message is null");
        }
        // 2.将用户的消息加入redis
        JSONObject message = new JSONObject()
                .fluentPut("role", userAIMessageVO.getRole())
                .fluentPut("content", userAIMessageVO.getContent());
        redisTemplate.opsForList().rightPush(buildUserMessageKey(), message.toJSONString());
    }

    /**
     * 构造历史消息数组
     *
     * @return JSONArray 历史消息数组
     */
    private JSONArray buildHistoryMessageArray() {
        // 将用户的历史消息构造为JSONArray数组
        List<Object> messages = redisTemplate.opsForList().range(buildUserMessageKey(), 0, -1);
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
        UserAIMessageVO message = new UserAIMessageVO(
                DeepSeekConstant.ROLE_ASSISTANT,
                Optional.ofNullable(content).orElse("")
        );
        addMessage(message); // 复用已有添加逻辑
    }

    /**
     * 从响应中提取答案
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

    /**
     * 请求AI获取学校信息
     *
     * @param schoolAIRequestDTO 大学AI请求DTO
     * @return SchoolAIMessageVO 大学AI请求VO
     */
    @Override
    public SchoolAIMessageVO schoolInformation(SchoolAIRequestDTO schoolAIRequestDTO) {
        // 1.获取需要请求的学校名
        String schoolName = schoolMapper.selectBySchoolId(schoolAIRequestDTO.getSchoolId()).getSchoolName();
        // 2.封装问题
        String question = "你现在是" + schoolName + "的AI助手，请给我介绍你们的学校";
        Request request = buildRequest(question);
        // 3.发起请求并获取回答
        String answer = executeRequest(request);
        log.info(DeepSeekConstant.ROLE_ASSISTANT + "{}", answer);
        return new SchoolAIMessageVO(DeepSeekConstant.ROLE_ASSISTANT, answer);
    }

    /**
     * 请求AI获取专业信息
     *
     * @param majorAIRequestDTO 专业AI请求DTO
     * @return MajorAIMessageVO 专业AI请求VO
     */
    @Override
    public MajorAIMessageVO majorInformation(MajorAIRequestDTO majorAIRequestDTO) {
        // 1.获取需要请求的专业名并封装问题
        String majorName = majorMapper.selectById(majorAIRequestDTO.getMajorId()).getMajorName();
        String question = "请为我介绍" + majorName + "这个专业";
        // 2.构建请求
        Request request = buildRequest(question);
        // 3.发起请求并获取回答
        String answer = executeRequest(request);
        log.info(DeepSeekConstant.ROLE_ASSISTANT + "{}", answer);
        return new MajorAIMessageVO(DeepSeekConstant.ROLE_ASSISTANT, answer);
    }

    /**
     * 构建请求
     *
     * @param question 请求问题
     * @return Request 请求
     */
    private Request buildRequest(String question) {
        // 1.将问题封装为JSON数组
        JSONArray message = new JSONArray();
        JSONObject initMessage = new JSONObject();
        initMessage.put("role", "system");
        initMessage.put("content", question);
        message.add(initMessage);
        // 2.构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", DeepSeekConstant.MODEL_NAME);
        requestBody.put("messages", message);
        requestBody.put("stream", false);
        // 3.构建新的请求，请求DeepSeekAPI
        return new Request.Builder()
                .url(DeepSeekConstant.API_URL)
                .addHeader("Authorization", "Bearer " + DeepSeekConstant.API_KEY)
                .addHeader("Content-Type", DeepSeekConstant.PARSE_SET)
                .post(RequestBody.create(
                        requestBody.toJSONString(),
                        MediaType.parse(DeepSeekConstant.PARSE_SET)))
                .build();
    }

    /**
     * 请求DeepSeekAPI并封装回答
     *
     * @param request 请求
     * @return answer 回答
     */
    private String executeRequest(Request request) {
        // 1.获取请求客户端
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        // 2.发起请求并接收响应
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                // 2.1响应失败，记录错误日志并返回
                log.info(DeepSeekConstant.RESPONSE_ERROR_CONSTANT);
                throw new DeepSeekAPIErrorException(DeepSeekConstant.RESPONSE_ERROR_CONSTANT);
            }
            // 2.2获取响应体
            JSONObject responseJson = null;
            if (response.body() != null) {
                responseJson = JSON.parseObject(response.body().string());
            }
            // 2.3解析响应体获取回答并返回
            return extractAnswer(responseJson);
        } catch (IOException e) {
            // 2.4请求失败则报错
            log.info(DeepSeekConstant.REQUEST_ERROR_CONSTANT);
            throw new DeepSeekAPIErrorException(DeepSeekConstant.REQUEST_ERROR_CONSTANT);
        }
    }
}


