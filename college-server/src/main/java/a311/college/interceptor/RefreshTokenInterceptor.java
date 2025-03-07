package a311.college.interceptor;

import a311.college.dto.login.UserPhoneLoginDTO;
import a311.college.redis.RedisKeyConstant;
import a311.college.thread.ThreadLocalUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;


public class RefreshTokenInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object hanlder) throws Exception {
        // 1.获取请求头的token
        String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token)) {
            // 1.1没有携带该请求头，放行至下一个拦截器
            return true;
        }
        // 2.根据不同的请求路径和token获取redis中的用户登录状态
        String url = request.getRequestURL().toString();
        String[] split = url.split("/");
        int length = split.length;
        if (split[length - 1].equals("login")) {
            // 此时为普通的账号密码登录
            String key = RedisKeyConstant.USER_KEY + token;
            String s = stringRedisTemplate.opsForValue().get(key);
        } else if ((split[length - 2] + "/" + split[length - 1]).equals("login/phone")) {
            // 此时为手机号 + 验证码登录
            String key = RedisKeyConstant.USER_PHONE_KEY + token;
            Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
            // 3.判断用户是否存在
            if (userMap.isEmpty()) {
                // 3.1用户不存在，放行到下一个拦截器
                return true;
            }
            UserPhoneLoginDTO userPhoneLoginDTO = BeanUtil.fillBeanWithMap(userMap,
                    new UserPhoneLoginDTO(), false);
            // 4.将用户信息保存到ThreadLocal
            ThreadLocalUtil.setCurrentId(userPhoneLoginDTO.getId());
            // 5.刷新用户登录有效期
            stringRedisTemplate.expire(key, RedisKeyConstant.USER_TTL, TimeUnit.SECONDS);
            return true;
        }
        return true;
    }
}
