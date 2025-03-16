package a311.college.redis;

/**
 * redis缓存常量
 */
public class RedisKeyConstant {

    // 用户验证码
    // 用户注册验证码
    public static final String USER_REGISTER_CODE_KEY = "college:user:code:register";
    // 用户修改密码验证码
    public static final String USER_EDIT_CODE_KEY = "college:user:code:edit:";
    // 用户注销验证码
    public static final String USER_DELETE_CODE_KEY = "college:user:code:delete:";

    // 用户登录凭证
    public static final String USER_KEY_TOKEN = "college";
    public static final String USER_KEY = "college:user:login:";

    // 验证码过期时间
    public static final Long USER_CODE_TTL = 60L;

    // 用户过期时间
    public static final Long USER_TTL = 900L;

    // 验证码有效期
    public static final String CODE_TIME_MESSAGE = "验证码30S内有效";


    // 大学相关
    // 大学
    public static final String COLLEGE_CACHE_KEY = "college:area:";

    // 大学过期时间
    public static final Long COLLEGE_CACHE_TTL = 1800L;

}
