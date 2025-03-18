package a311.college.constant.redis;

/**
 * 用户缓存键常量
 */
public class UserRedisKey {
    // 用户验证码
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
    public static final Long USER_TTL = 1800L;

    // 验证码有效期
    public static final String CODE_TIME_MESSAGE = "验证码30S内有效";
}
