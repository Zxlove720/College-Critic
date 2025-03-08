package a311.college.service;

import a311.college.dto.user.PasswordEditDTO;
import a311.college.dto.user.PhoneLoginDTO;
import a311.college.dto.user.UserDTO;
import a311.college.dto.login.LoginDTO;
import a311.college.dto.user.UserPageQueryDTO;
import a311.college.entity.User;
import a311.college.result.PageResult;

/**
 * 用户相关服务
 */
public interface UserService {

    String login(LoginDTO loginDTO);

    String sendCode(String phone);

    String phoneLogin(PhoneLoginDTO phoneLoginDTO);

    PageResult<User> pageSelect(UserPageQueryDTO userPageQueryDTO);

    User selectById(Long id);

    void deleteById(Long id);

    void save(UserDTO userDTO);

    void update(UserDTO userDTO);

    void changeStatus(Integer status, Long id);

    void editPassword(PasswordEditDTO passwordEditDTO);

}
