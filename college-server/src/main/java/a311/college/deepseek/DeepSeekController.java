package a311.college.deepseek;

import a311.college.entity.ai.Message;
import a311.college.entity.ai.UserRequest;
import a311.college.result.Result;
import a311.college.service.DeepSeekService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理用户发起的AI请求
 */
@RestController
@RequestMapping("/ai")
public class DeepSeekController {

    private final DeepSeekService deepSeekService;

    public DeepSeekController(DeepSeekService deepSeekService) {
        this.deepSeekService = deepSeekService;
    }

    /**
     * 回答用户问题
     *
     * @param request 用户请求
     * @return Result<Void>
     */
    @PostMapping("/answer")
    public Result<Message> responseQuestion(@RequestBody UserRequest request) {
        return Result.success(deepSeekService.response(request));
    }
}
