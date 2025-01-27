package a311.college.service;

import a311.college.dao.UserLoginDTO;
import a311.college.entity.User;

/**
 * 用户相关服务
 */
public interface UserService {

    User login(UserLoginDTO userLoginDTO);
}
