package a311.college.service;

import a311.college.dto.school.SchoolDTO;
import a311.college.entity.ai.UserRequestAIMessage;
import a311.college.entity.ai.UserRequestAI;

public interface DeepSeekService {

    UserRequestAIMessage response(UserRequestAI request);

}
