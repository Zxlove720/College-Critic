package a311.college.service;

import a311.college.dto.user.*;
import a311.college.dto.login.UserLoginDTO;
import a311.college.entity.school.School;
import a311.college.result.LoginResult;
import a311.college.vo.user.UserVO;

import java.util.List;

/**
 * 用户相关服务
 */
public interface UserService {

    LoginResult login(UserLoginDTO userLoginDTO);

    Integer checkUser(UserUsernameCheckDTO userUsernameCheckDTO);

    Integer checkPhone(UserPhoneCheckDTO userPhoneCheckDTO);

    void register(UserDTO userDTO);

    String sendEditCode(UserCodeDTO userCodeDTO);

    LoginResult editPassword(UserEditPasswordDTO userEditPasswordDTO);

    void layout(UserLayoutDTO userLayoutDTO);

    UserVO selectById(Long id);

    void addFavoriteSchool(UserAddFavoriteSchoolDTO userAddFavoriteSchoolDTO);

    void addFavoriteMajor(UserAddFavoriteMajorDTO userAddFavoriteMajorDTO);

    List<School> showFavorite();

    void update(UserDTO userDTO);

    String sendDeleteCode(UserCodeDTO userCodeDTO);

    void deleteUser(UserDeleteDTO userDeleteDTO);

    List<String> showComment();

}
