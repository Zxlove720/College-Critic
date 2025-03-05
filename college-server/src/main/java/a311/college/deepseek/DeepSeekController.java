package a311.college.deepseek;

import a311.college.entity.ai.Message;
import a311.college.result.Result;
import a311.college.service.DeepSeekService;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理用户发起的AI请求
 */
@RestController
@RequestMapping("/ai")
@Log4j
public class DeepSeekController {

    private final DeepSeekService deepSeekService;

    public DeepSeekController(DeepSeekService deepSeekService) {
        this.deepSeekService = deepSeekService;
    }

    /**
     * 回答用户问题
     *
     * @param message
     * @return Result<Void>
     */
    @PostMapping
    public Result<Message> responseQuestion(@RequestBody Message message) {
        return Result.success(deepSeekService.response(message));
    }
}
