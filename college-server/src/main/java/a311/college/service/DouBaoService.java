package a311.college.service;

import a311.college.dto.ai.UserAIRequestDTO;
import a311.college.vo.ai.UserAIMessageVO;

public interface DouBaoService {

    UserAIMessageVO response(UserAIRequestDTO request);
}
