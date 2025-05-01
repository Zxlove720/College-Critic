package a311.college.controller.ai;

import a311.college.dto.ai.UserAIRequestDTO;
import a311.college.result.Result;
import a311.college.service.AIService;
import a311.college.vo.ai.UserAIMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AIController {

    private final AIService aiService;

    @Autowired
    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    /**
     * AI对话
     *
     * @param userAIRequestDTO 用户ai对话DTO
     * @return Result<UserAIMessageVO>
     */
    @PostMapping
    public Result<UserAIMessageVO> handleChat(@RequestBody UserAIRequestDTO userAIRequestDTO) {
        return Result.success(aiService.responseQuestion(userAIRequestDTO));
    }

    /**
     * 清除历史对话
     */
    @PostMapping("/clearHistory")
    public Result<Void> clearHistory() {
        aiService.clearChatHistory();
        return Result.success();
    }
}
