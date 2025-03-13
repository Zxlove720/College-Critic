package a311.college.service;

import a311.college.dto.login.PhoneLoginDTO;
import a311.college.dto.user.AddFavoriteDTO;
import a311.college.dto.user.CodeDTO;
import a311.college.dto.user.PasswordEditDTO;
import a311.college.dto.user.UserDTO;
import a311.college.dto.login.LoginDTO;
import a311.college.result.LoginResult;
import a311.college.vo.CollegeSimpleVO;
import a311.college.vo.UserVO;

import java.util.List;

/**
 * 用户相关服务
 */
public interface UserService {

    LoginResult login(LoginDTO loginDTO);

    String sendCode(CodeDTO codeDTO);

    LoginResult phoneLogin(PhoneLoginDTO phoneLoginDTO);

    String sendEditCode(CodeDTO codeDTO);

    LoginResult editPassword(PasswordEditDTO passwordEditDTO);

    UserVO selectById(Long id);

    void addFavorite(AddFavoriteDTO addFavoriteDTO);

    List<CollegeSimpleVO> showFavorite();

    void deleteById(Long id);

    void update(UserDTO userDTO);
}
