package a311.college.service.impl;

import a311.college.dto.ai.UserAIRequestDTO;
import a311.college.service.AIService;
import a311.college.vo.ai.UserAIMessageVO;
import org.springframework.stereotype.Service;

@Service
public class AIServiceImpl implements AIService {

    @Override
    public UserAIMessageVO responseQuestion(UserAIRequestDTO userAIRequestDTO) {
        return null;
    }

    @Override
    public void clearChatHistory() {

    }
}
