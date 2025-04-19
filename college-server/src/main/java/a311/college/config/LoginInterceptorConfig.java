//package a311.college.config;
//
//import a311.college.interceptor.LoginInterceptor;
//import a311.college.interceptor.RefreshTokenInterceptor;
//import jakarta.annotation.Resource;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class LoginInterceptorConfig implements WebMvcConfigurer {
//
//    @Resource
//    private StringRedisTemplate stringRedisTemplate;
//
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册登录拦截器
//        registry.addInterceptor(new LoginInterceptor())
//                .excludePathPatterns(
//                        "/users/login",         // 用户登录
//                        "/upload",              // 用户上传头像
//                        "/users/register",      // 用户注册
//                        "/users/checkUsername", // 检查用户名是否可用
//                        "/users/checkPhone",    // 检查手机号是否可用
//                        "/users/editCode",      // 用户请求修改密码发送验证码
//                        "/users/edit",          // 用户修改密码
//                        "/schools/schools",     // 分页查询学校
//                        "/schools/search",      // 用户搜索
//                        "/schools/searchList",  // 搜索提示
//                        "/schools/hotSchool1",  // 获取本省热门本科学校
//                        "/schools/hotSchool2",  // 获取本省热门专科学校
//                        "/schools/hotSchool3",  // 获取外省热门本科学校
//                        "/schools/hotSchool4",  // 获取外省热门专科学校
//                        "/schools/hotSchool",   // 获取热门学校
//                        "/schools/hotMajor1",   // 获取热门本科专业
//                        "/schools/hotMajor2",   // 获取热门专科专业
//                        "/schools/hotRank",     // 获取热门学校排行榜
//                        "/schools/basic"        // 获取强基计划学校
//                ).order(1);
//        // 注册登录状态刷新拦截器
//        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate))
//                .order(0);
//    }
//
//}
