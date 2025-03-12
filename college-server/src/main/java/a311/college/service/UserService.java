package a311.college.service;

import a311.college.dto.login.PhoneLoginDTO;
import a311.college.dto.user.AddFavoriteDTO;
import a311.college.dto.user.UserDTO;
import a311.college.dto.login.LoginDTO;
import a311.college.result.LoginResult;
import a311.college.vo.UserVO;

/**
 * 用户相关服务
 */
public interface UserService {

    LoginResult login(LoginDTO loginDTO);

    String sendCode(String phone);

    LoginResult phoneLogin(PhoneLoginDTO phoneLoginDTO);

    UserVO selectById(Long id);

    void deleteById(Long id);

    void save(UserDTO userDTO);

    void update(UserDTO userDTO);

    void addFavorite(AddFavoriteDTO addFavoriteDTO);
}
