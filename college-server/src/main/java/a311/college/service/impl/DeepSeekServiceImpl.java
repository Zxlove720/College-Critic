package a311.college.service.impl;

import a311.college.entity.ai.Message;
import a311.college.service.DeepSeekService;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class DeepSeekServiceImpl implements DeepSeekService {


    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public DeepSeekServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 回答用户问题
     * @param message 用户问题
     * @return Message
     */
    @Override
    public Message response(Message message) {
        // 创建连接对象
        OkHttpClient client = new OkHttpClient.Builder()
                // 设置连接超时
                .connectTimeout(60, TimeUnit.SECONDS)
                // 设置读取超时
                .readTimeout(60, TimeUnit.SECONDS)
                // 设置写入超时
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        addSystemMessage("你现在是一个高考志愿填报系统的后台AI，请认真分析用户的需求");
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
}
