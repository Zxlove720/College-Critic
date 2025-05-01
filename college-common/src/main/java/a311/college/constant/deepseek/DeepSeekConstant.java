package a311.college.constant.deepseek;

public class DeepSeekConstant {

    public static final String API_URL = "https://ark.cn-beijing.volces.com/api/v3/chat/completions";

    public static final String API_KEY = System.getenv("ARK_API_KEY");

    public static final String INIT_CONSTANT = "你是一个服务于高考志愿填报系统的AI";

    public static final String MODEL_NAME = "doubao-1-5-pro-256k-250115";

    public static final String PARSE_SET = "application/json";

    public static final String RESPONSE_ERROR_CONSTANT = "响应异常";

    public static final String REQUEST_ERROR_CONSTANT = "请求失败";

    public static final String ROLE_SYSTEM = "system";

    public static final String ROLE_ASSISTANT = "assistant";
}
