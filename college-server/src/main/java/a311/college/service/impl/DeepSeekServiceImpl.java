package a311.college.service.impl;

import a311.college.constant.deepseek.APIConstant;
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
        addSystemMessage("你是一个服务于高考志愿填报系统的AI");
        // 将用户消息添加到历史
        addUserMessage(request.getMessage().getContent());
        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("messages", buildMessageArray());
        requestBody.put("stream", false);

        Request seekRequest = new Request.Builder()
                .url(APIConstant.API_URL)
                .addHeader("Authorization", "Bearer " + APIConstant.API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(
                        requestBody.toJSONString(),
                        MediaType.parse("application/json")))
                .build();
        try (Response response = client.newCall(seekRequest).execute()) {
            if (!response.isSuccessful()) {
                log.info("用户请求失败");
                return new Message("assistant", "error");
            }
            JSONObject responseJson = JSON.parseObject(response.body().string());
            String answer = extractAnswer(responseJson);
            // 添加助手回复到历史
            addAssistantMessage(answer);
            log.info("assistant：{}", answer);
            return new Message("assistant", answer);
        } catch (IOException e) {
            log.info("请求异常");
        }
        return new Message("assistant", "error");
    }

    private JSONArray buildMessageArray() {
        JSONArray messages = new JSONArray();
        messages.addAll(messageHistory);
        return messages;
    }

    private void addSystemMessage(String content) {
        messageHistory.add(new JSONObject()
                .fluentPut("role", "system")
                .fluentPut("content", content));
    }

    private void addUserMessage(String content) {
        messageHistory.add(new JSONObject()
                .fluentPut("role", "user")
                .fluentPut("content", content));
    }

    private void addAssistantMessage(String content) {
        messageHistory.add(new JSONObject()
                .fluentPut("role", "assistant")
                .fluentPut("content", content));
    }

    private String extractAnswer(JSONObject response) {
        return response.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }
}


