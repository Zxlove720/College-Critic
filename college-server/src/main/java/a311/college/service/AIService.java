package a311.college.service;

import a311.college.dto.ai.UserAIRequestDTO;
import a311.college.vo.ai.UserAIMessageVO;

public interface AIService {

    UserAIMessageVO responseQuestion(UserAIRequestDTO userAIRequestDTO);

    void clearChatHistory();

}
