package a311.college.controller.doubao;

import a311.college.dto.ai.UserAIRequestDTO;
import a311.college.result.Result;
import a311.college.service.DouBaoService;
import a311.college.vo.ai.UserAIMessageVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class DouBaoController {

    private final DouBaoService douBaoService;

    @Autowired
    public DouBaoController(DouBaoService douBaoService) {
        this.douBaoService = douBaoService;
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
        return Result.success(douBaoService.response(request));
    }
}
