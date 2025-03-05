package a311.college.service;

import a311.college.entity.ai.Message;
import a311.college.entity.ai.UserRequest;

public interface DeepSeekService {

    Message response(UserRequest request);

}
