package a311.college.service;

import a311.college.entity.ai.UserAIRequestMessage;
import a311.college.entity.ai.UserAIRequest;

public interface DeepSeekService {

    UserAIRequestMessage response(UserAIRequest request);

}
