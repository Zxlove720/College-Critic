package a311.college.interceptor;

import a311.college.thread.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 统一拦截器：用户登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 用户登录拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // 1.判断是否需要进行拦截（ThreadLocal中是否存在用户）
        if (ThreadLocalUtil.getCurrentId() == null) {
            // 1.1没有用户（表示用户未登录），那么进行拦截并设置返回状态码
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        // 2.ThreadLocal中有用户，直接放行
        return true;
    }
}
