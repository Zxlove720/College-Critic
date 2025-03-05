package a311.college.deepseek;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class DeepSeekDemo {
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String API_KEY = System.getenv("DEEPSEEK_API_KEY"); // 替换实际API密钥
    
    // 存储对话历史
    private static final List<JSONObject> messageHistory = new ArrayList<>();

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        Scanner scanner = new Scanner(System.in);

        // 初始化系统角色
        addSystemMessage("你是一个专业的Java技术专家，用简洁易懂的方式回答技术问题");

        System.out.println("输入问题开始对话（输入exit退出）:");
        while (true) {
            System.out.print("用户: ");
            String userInput = scanner.nextLine();
            if ("exit".equalsIgnoreCase(userInput)) break;

            // 添加用户消息到历史
            addUserMessage(userInput);

            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "deepseek-chat");
            requestBody.put("messages", buildMessageArray());
            requestBody.put("stream", false);

            Request request = new Request.Builder()
                    .url(API_URL)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(
                            requestBody.toJSONString(), 
                            MediaType.parse("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.err.println("请求失败: " + response.code());
                    continue;
                }

                JSONObject responseJson = JSON.parseObject(response.body().string());
                String answer = extractAnswer(responseJson);
                
                // 添加助手回复到历史
                addAssistantMessage(answer);
                System.out.println("助手: " + answer);

            } catch (IOException e) {
                System.err.println("请求异常: " + e.getMessage());
            }
        }
        scanner.close();
    }

    // 以下为辅助方法
    private static JSONArray buildMessageArray() {
        JSONArray messages = new JSONArray();
        for (JSONObject msg : messageHistory) {
            messages.add(msg);
        }
        return messages;
    }

    private static void addSystemMessage(String content) {
        messageHistory.add(new JSONObject()
                .fluentPut("role", "system")
                .fluentPut("content", content));
    }

    private static void addUserMessage(String content) {
        messageHistory.add(new JSONObject()
                .fluentPut("role", "user")
                .fluentPut("content", content));
    }

    private static void addAssistantMessage(String content) {
        messageHistory.add(new JSONObject()
                .fluentPut("role", "assistant")
                .fluentPut("content", content));
    }

    private static String extractAnswer(JSONObject response) {
        return response.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }
}