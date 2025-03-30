package a311.college.service;

import a311.college.dto.ai.MajorAIRequestDTO;
import a311.college.dto.ai.SchoolAIRequestDTO;
import a311.college.vo.ai.MajorAIMessageVO;
import a311.college.vo.ai.SchoolAIMessageVO;
import a311.college.vo.ai.UserAIMessageVO;
import a311.college.dto.ai.UserAIRequestDTO;

public interface DeepSeekService {

    UserAIMessageVO response(UserAIRequestDTO request);

    SchoolAIMessageVO schoolInformation(SchoolAIRequestDTO schoolAIRequestDTO);

    MajorAIMessageVO majorInformation(MajorAIRequestDTO majorAIRequestDTO);
}
