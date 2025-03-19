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

    LoginResult login(UserLoginDTO userLoginDTO);

    void register(UserDTO userDTO);

    String sendEditCode(UserCodeDTO userCodeDTO);

    LoginResult editPassword(UserEditPasswordDTO userEditPasswordDTO);

    void layout(UserLayoutDTO userLayoutDTO);

    UserVO selectById(Long id);

    void addFavorite(UserAddFavoriteDTO userAddFavoriteDTO);

    List<SchoolSimpleVO> showFavorite();

    void update(UserDTO userDTO);

    String sendDeleteCode(UserCodeDTO userCodeDTO);

    void deleteUser(UserDeleteDTO userDeleteDTO);

    List<String> showComment();

    Integer checkUser(UserUsernameCheckDTO userUsernameCheckDTO);

    Integer checkPhone(UserPhoneCheckDTO userPhoneCheckDTO);
}
