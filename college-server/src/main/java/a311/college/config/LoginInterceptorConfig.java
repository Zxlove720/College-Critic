package a311.college.config;

import a311.college.interceptor.LoginInterceptor;
import a311.college.interceptor.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginInterceptorConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/users/login",         // 用户登录
                        "/upload",              // 用户上传头像
                        "/users/register",      // 用户注册
                        "/users/checkUsername", // 检查用户名是否可用
                        "/users/checkPhone",    // 检查手机号是否可用
                        "/users/editCode",      // 用户请求修改密码发送验证码
                        "/users/edit",          // 用户修改密码
                        "/schools/scenery",     // 获取首页校园风光
                        "/schools/page",        // 分页查询学校
                        "/schools/hotSchool",   // 获取热门学校
                        "/schools/hotMajor1",   // 获取热门本科专业
                        "/schools/hotMajor2",   // 获取热门专科专业
                        "/schools/scenery"      // 获取首页校园风光
                ).order(1);
        // 注册登录状态刷新拦截器
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate))
                .order(0);
    }

}
