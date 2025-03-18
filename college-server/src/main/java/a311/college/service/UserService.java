package a311.college.service;

import a311.college.dto.user.*;
import a311.college.dto.login.UserLoginDTO;
import a311.college.result.LoginResult;
import a311.college.vo.SchoolSimpleVO;
import a311.college.vo.UserVO;

import java.util.List;

/**
 * 用户相关服务
 */
public interface UserService {

    LoginResult login(UserLoginDTO loginDTO);

    void register(UserDTO userDTO);

    String sendEditCode(UserCodeDTO codeDTO);

    LoginResult editPassword(UserEditPasswordDTO passwordEditDTO);

    void layout(UserLayoutDTO layoutDTO);

    UserVO selectById(Long id);

    void addFavorite(UserAddFavoriteDTO addFavoriteDTO);

    List<SchoolSimpleVO> showFavorite();

    void update(UserDTO userDTO);

    String sendDeleteCode(UserCodeDTO codeDTO);

    void deleteUser(UserDeleteDTO deleteDTO);

    List<String> showComment();

    Integer checkUser(UserUsernameCheckDTO checkUserDTO);

    Integer checkPhone(UserPhoneCheckDTO checkPhoneDTO);
}
