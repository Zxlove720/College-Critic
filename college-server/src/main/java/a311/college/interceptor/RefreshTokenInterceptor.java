package a311.college.interceptor;

import a311.college.dto.login.PhoneLoginDTO;
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

/**
 * 刷新用户登录凭据拦截器
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 刷新登录凭证拦截器
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object hanlder) throws Exception {
        // 1.获取请求头的token
        String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token)) {
            // 1.1没有携带该请求头，放行至下一个拦截器
            return true;
        }
        // 2.根据token获取redis中缓存的用户
        String key = RedisKeyConstant.USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        // 3.判断用户是否存在
        if (userMap.isEmpty()) {
            // 3.1用户不存在，放行到下一个拦截器
            return true;
        }
        PhoneLoginDTO userPhoneLoginDTO = BeanUtil.fillBeanWithMap(userMap,
                new PhoneLoginDTO(), false);
        // 4.将用户信息保存到ThreadLocal
        ThreadLocalUtil.setCurrentId(userPhoneLoginDTO.getId());
        // 5.刷新用户登录有效期
        stringRedisTemplate.expire(key, RedisKeyConstant.USER_TTL, TimeUnit.SECONDS);
        // 6.放行至下一个拦截器
        return true;
    }

    /**
     * 响应时从ThreadLocal中移除用户，释放资源
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        // 移除用户
        ThreadLocalUtil.removeCurrentId();
    }

}
