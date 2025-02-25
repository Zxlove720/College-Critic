package a311.college.constant;

/**
 * Knife4j配置常量类
 *
 * @author wzb
 * @since 2025-2-25
 */
public class Knife4jConstant {

    /**
     * Knife4j的接口分组和分组路径
     */
    public static class Knife4j {

        /**
         * 接口分组
         */
        // 用户服务接口
        public static final String USER_SERVICE = "用户相关服务";
        // 大学服务接口
        public static final String COLLEGE_SERVICE = "大学相关服务";

        /**
         * 接口路径分组
         */
        // 用户服务接口路径
        public static final String USER_PATH = "a311.college.controller.UserController";
        // 大学服务接口路径
        public static final String COLLEGE_PATH = "a311.college.controller.CollegeController";
    }

    /**
     * 编码常量
     */
    public static class Charset {

        /**
         * 编码格式设置
         */
        public static final String JSON_TYPE_UTF8_CHARSET = "application/json;charset=UTF-8";
    }
}
