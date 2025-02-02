package a311.college.interceptor;

import a311.college.constant.JWTClaimsConstant;
import a311.college.jwt.JWTUtils;
import a311.college.properties.JWTProperties;
import a311.college.thread.ThreadLocalUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JWTTokenInterceptor implements HandlerInterceptor {

    private final JWTProperties jwtProperties;

    @Autowired
    public JWTTokenInterceptor (JWTProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断当前拦截方法是否是Controller中的方法，如果不是，那么直接放行
        if (!(handler instanceof HandlerMethod)) {
            // 不是方法，直接放行
            return true;
        }
        // 获取JWT令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());
        // 校验JWT令牌
        try {
            log.info("JWT令牌校验：{}", token);
            Claims claims = JWTUtils.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.parseLong(claims.get(JWTClaimsConstant.USER_ID).toString());
            log.info("当前用户id为：{}", userId);
            ThreadLocalUtil.setCurrentId(userId);
            // 放行
            return true;
        } catch (Exception e) {
            // 解析过程发生错误，不通过
            response.setStatus(401);
            return false;
        }
    }
}
