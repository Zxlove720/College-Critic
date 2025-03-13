package a311.college.service;

import a311.college.dto.user.*;
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

    void register(UserDTO userDTO);

    String sendEditCode(CodeDTO codeDTO);

    LoginResult editPassword(PasswordEditDTO passwordEditDTO);

    void layout(LayoutDTO layoutDTO);

    String sendDeleteCode(CodeDTO codeDTO);

    UserVO selectById(Long id);

    void addFavorite(AddFavoriteDTO addFavoriteDTO);

    List<CollegeSimpleVO> showFavorite();

    void deleteUser(DeleteDTO deleteDTO);

    void update(UserDTO userDTO);

}
