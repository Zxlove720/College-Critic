package a311.college.constant.user;

/**
 * 用户服务常量
 */
public class UserErrorConstant {

    // 用户被禁用了
    public static final Integer DISABLE = 0;

    public static final String PHONE_NUMBER_ERROR = "手机号格式错误-500";

    public static final String ACCOUNT_LOCKED = "用户被锁定-500";

    public static final String ACCOUNT_NOT_FOUND = "用户不存在，请先注册-500";

    public static final String PASSWORD_ERROR = "密码错误-500";

    public static final String ALREADY_EXISTS = "用户已存在-500";

    public static final String CODE_ERROR = "验证码错误-500";

    public static final String RE_ADDITION = "重复添加-500";

    public static final String USER_SCHOOL_ERROR = "用户收藏学校查询错误";

    public static final String USER_MAJOR_ERROR = "用户收藏专业查询错误";

    public static final String USER_COMMENT_ERROR = "用户评论查询错误";

}
