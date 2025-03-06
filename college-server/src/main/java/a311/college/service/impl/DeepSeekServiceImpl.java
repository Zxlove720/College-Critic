package a311.college.service.impl;

import a311.college.constant.deepseek.DeepSeekConstant;
import a311.college.entity.ai.Message;
import a311.college.entity.ai.UserRequest;
import a311.college.service.DeepSeekService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * DeepSeek多轮对话
 */
@Slf4j
@Service
public class DeepSeekServiceImpl implements DeepSeekService {

    // 存储对话历史
    private static final List<JSONObject> messageHistory = new ArrayList<>();

    /**
     * 请求DeepSeekAPI并获取响应
     * @param request 请求
     * @return Message 将DeepSeek的响应封装为Message对象返回
     */
    @Override
    public Message response(UserRequest request) {
        OkHttpClient client = new OkHttpClient.Builder()
                // 设置连接超时时间
                .connectTimeout(60, TimeUnit.SECONDS)
                // 设置读取超时时间
                .readTimeout(60, TimeUnit.SECONDS)
                // 设置写入超时时间
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        // 初始化AI
        addSystemMessage();
        // 将用户消息添加到历史
        addUserMessage(request.getMessage().getContent());
        // 构建请求体
        JSONObject requestBody = new JSONObject();
        // 选择模型
        requestBody.put("model", DeepSeekConstant.MODEL_NAME);
        // 构建消息
        requestBody.put("messages", buildMessageArray());
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
                return new Message(DeepSeekConstant.ROLE_ASSISTANT, "error");
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
            return new Message(DeepSeekConstant.ROLE_ASSISTANT, answer);
        } catch (IOException e) {
            log.info(DeepSeekConstant.REQUEST_CONSTANT);
        }
        return new Message(DeepSeekConstant.ROLE_ASSISTANT, "error");
    }

    private JSONArray buildMessageArray() {
        JSONArray messages = new JSONArray();
        messages.addAll(messageHistory);
        return messages;
    }

    /**
     * 初始化AI
     */
    private void addSystemMessage() {
        messageHistory.add(new JSONObject()
                .fluentPut("role", DeepSeekConstant.ROLE_SYSTEM)
                .fluentPut("content", DeepSeekConstant.INIT_CONSTANT));
    }

    /**
     * 添加用户问题
     * @param content 用户问题
     */
    private void addUserMessage(String content) {
        messageHistory.add(new JSONObject()
                .fluentPut("role", DeepSeekConstant.ROLE_USER)
                .fluentPut("content", content));
    }

    /**
     * 添加回答
     * @param content 回答
     */
    private void addAssistantMessage(String content) {
        messageHistory.add(new JSONObject()
                .fluentPut("role", DeepSeekConstant.ROLE_ASSISTANT)
                .fluentPut("content", content));
    }

    /**
     * 解析回答
     * @param response 响应
     * @return String 回答
     */
    private String extractAnswer(JSONObject response) {
        return response.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }
}


