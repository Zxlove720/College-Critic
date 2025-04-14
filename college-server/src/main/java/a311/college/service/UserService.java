package a311.college.service;

import a311.college.dto.major.MajorDTO;
import a311.college.dto.query.PageQueryDTO;
import a311.college.dto.school.SchoolDTO;
import a311.college.dto.user.*;
import a311.college.dto.login.UserLoginDTO;
import a311.college.entity.major.Major;
import a311.college.entity.school.School;
import a311.college.result.LoginResult;
import a311.college.result.PageResult;
import a311.college.vo.user.UserVO;

/**
 * 用户相关服务
 */
public interface UserService {

    LoginResult login(UserLoginDTO userLoginDTO);

    Integer checkUsername(UserUsernameCheckDTO userUsernameCheckDTO);

    Integer checkPhone(UserPhoneCheckDTO userPhoneCheckDTO);

    void register(UserDTO userDTO);

    String sendEditCode(UserCodeDTO userCodeDTO);

    LoginResult editPassword(UserEditPasswordDTO userEditPasswordDTO);

    void layout();

    UserVO showMe(Long id);

    PageResult<School> showFavoriteSchool(PageQueryDTO pageQueryDTO);

    PageResult<Major> showFavoriteMajor(PageQueryDTO pageQueryDTO);

    void deleteSchool(SchoolDTO schoolDTO);

    void deleteMajor(MajorDTO majorDTO);

    PageResult<String> showComment(PageQueryDTO pageQueryDTO);

    void deleteComment(UserCommentDTO userCommentDTO);

    void update(UserDTO userDTO);

    String sendDeleteCode(UserCodeDTO userCodeDTO);

    void deleteUser(UserDeleteDTO userDeleteDTO);

}
