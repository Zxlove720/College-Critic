package a311.college.service;

import a311.college.dto.ai.SchoolAIRequestDTO;
import a311.college.vo.ai.SchoolAIRequestMessageVO;
import a311.college.vo.ai.UserAIRequestMessageVO;
import a311.college.dto.ai.UserAIRequestDTO;

public interface DeepSeekService {

    UserAIRequestMessageVO response(UserAIRequestDTO request);

    SchoolAIRequestMessageVO schoolInformation(SchoolAIRequestDTO schoolAIRequestDTO);

}
