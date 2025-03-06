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

    @Override
    public Message response(UserRequest request) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        addSystemMessage();
        // 将用户消息添加到历史
        addUserMessage(request.getMessage().getContent());
        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", DeepSeekConstant.MODEL_NAME);
        requestBody.put("messages", buildMessageArray());
        requestBody.put("stream", request.getStream());

        Request seekRequest = new Request.Builder()
                .url(DeepSeekConstant.API_URL)
                .addHeader("Authorization", "Bearer " + DeepSeekConstant.API_KEY)
                .addHeader("Content-Type", DeepSeekConstant.PARSE_SET)
                .post(RequestBody.create(
                        requestBody.toJSONString(),
                        MediaType.parse(DeepSeekConstant.PARSE_SET)))
                .build();
        try (Response response = client.newCall(seekRequest).execute()) {
            if (!response.isSuccessful()) {
                log.info(DeepSeekConstant.ERROR_CONSTANT);
                return new Message(DeepSeekConstant.ROLE_ASSISTANT, "error");
            }
            JSONObject responseJson = null;
            if (response.body() != null) {
                responseJson = JSON.parseObject(response.body().string());
            }
            String answer = null;
            if (responseJson != null) {
                answer = extractAnswer(responseJson);
            }
            // 添加助手回复到历史
            addAssistantMessage(answer);
            log.info(DeepSeekConstant.ROLE_ASSISTANT + "{}", answer);
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

    private void addSystemMessage() {
        messageHistory.add(new JSONObject()
                .fluentPut("role", DeepSeekConstant.ROLE_SYSTEM)
                .fluentPut("content", DeepSeekConstant.INIT_CONSTANT));
    }

    private void addUserMessage(String content) {
        messageHistory.add(new JSONObject()
                .fluentPut("role", DeepSeekConstant.ROLE_USER)
                .fluentPut("content", content));
    }

    private void addAssistantMessage(String content) {
        messageHistory.add(new JSONObject()
                .fluentPut("role", DeepSeekConstant.ROLE_ASSISTANT)
                .fluentPut("content", content));
    }

    private String extractAnswer(JSONObject response) {
        return response.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }
}


