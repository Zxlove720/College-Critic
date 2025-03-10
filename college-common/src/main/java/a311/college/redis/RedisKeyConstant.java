package a311.college.redis;

/**
 * redis缓存常量
 */
public class RedisKeyConstant {

    // 用户相关
    // 用户验证码
    public static final String USER_CODE_KEY = "college:user:code:";

    // 用户登录凭证
    public static final String USER_KEY = "college:user:token:";

    // 用户登录凭证
    public static final String USER_LOGIN_KEY = "college:user:login";

    // 验证码过期时间
    public static final Long USER_CODE_TTL = 300L;

    // 用户过期时间
    public static final Long USER_TTL = 1800L;

    // 验证码有效期
    public static final String CODE_TIME_MESSAGE = "验证码5分钟内有效";


    // 大学相关
    // 大学
    public static final String COLLEGE_CACHE_KEY = "college:area:";

    // 大学过期时间
    public static final Long COLLEGE_CACHE_TTL = 1800L;

}
