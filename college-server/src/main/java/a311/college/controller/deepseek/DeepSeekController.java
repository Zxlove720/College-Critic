package a311.college.controller.deepseek;

import a311.college.constant.API.APIConstant;
import a311.college.vo.ai.UserAIMessageVO;
import a311.college.dto.ai.UserAIRequestDTO;
import a311.college.result.Result;
import a311.college.service.DeepSeekService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理用户发起的AI请求
 */
@RestController
@RequestMapping("/ai")
@Tag(name = APIConstant.DEEP_SEEK_SERVICE)
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
    @Operation(summary = "DeepSeekApi回答问题")
    public Result<UserAIMessageVO> responseQuestion(@RequestBody UserAIRequestDTO request) {
        return Result.success(deepSeekService.response(request));
    }
}
