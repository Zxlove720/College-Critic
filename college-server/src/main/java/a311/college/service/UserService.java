package a311.college.service;

import a311.college.dto.query.major.PageQueryDTO;
import a311.college.dto.user.*;
import a311.college.dto.login.UserLoginDTO;
import a311.college.result.LoginResult;
import a311.college.vo.major.BriefMajorVO;
import a311.college.vo.school.BriefSchoolInfoVO;
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

    List<BriefSchoolInfoVO> showFavoriteSchool();

    List<BriefMajorVO> showFavoriteMajor(PageQueryDTO pageQueryDTO);

    void update(UserDTO userDTO);

    String sendDeleteCode(UserCodeDTO userCodeDTO);

    void deleteUser(UserDeleteDTO userDeleteDTO);

    List<String> showComment();

}
