package a311.college.controller.user;

import a311.college.constant.API.APIConstant;
import a311.college.dto.user.*;
import a311.college.dto.login.UserLoginDTO;
import a311.college.entity.school.School;
import a311.college.result.LoginResult;
import a311.college.result.Result;
import a311.college.service.UserService;
import a311.college.thread.ThreadLocalUtil;
import a311.college.vo.school.BriefSchoolInfoVO;
import a311.college.vo.user.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户请求控制器
 */
@Slf4j
@RestController
@RequestMapping("/users")
@Tag(name = APIConstant.USER_SERVICE)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录数据DTO
     * @return Result<String>
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<LoginResult> login(@RequestBody UserLoginDTO userLoginDTO) {
        String phone = userLoginDTO.getPhone();
        log.info("手机号为'{}'的用户正在登录", phone);
        return Result.success(userService.login(userLoginDTO));
    }

    /**
     * 用户名检查
     *
     * @param userUsernameCheckDTO 用户名检查DTO
     * @return Integer 0该用户名不可用 1该用户名可用
     */
    @PostMapping("/checkUsername")
    @Operation(summary = "用户名检查")
    public Result<Integer> checkUser(@RequestBody UserUsernameCheckDTO userUsernameCheckDTO) {
        return Result.success(userService.checkUser(userUsernameCheckDTO));
    }

    /**
     * 手机号检查
     *
     * @param userPhoneCheckDTO 手机号检查DTO
     * @return Integer 0该手机号不可用 1该手机号可用
     */
    @PostMapping("/checkPhone")
    @Operation(summary = "用户名检查")
    public Result<Integer> checkPhone(@RequestBody UserPhoneCheckDTO userPhoneCheckDTO) {
        return Result.success(userService.checkPhone(userPhoneCheckDTO));
    }

    /**
     * 用户注册
     *
     * @param userDTO 用户DTO
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<Void> register(@RequestBody UserDTO userDTO) {
        log.info("新用户注册...");
        userService.register(userDTO);
        return Result.success();
    }

    /**
     * 用户发送验证码修改密码
     *
     * @param userCodeDTO 验证码DTO
     * @return Result<String>
     */
    @PostMapping("/editCode")
    @Operation(summary = "用户发送验证码修改密码")
    public Result<String> sendEditCode(@RequestBody UserCodeDTO userCodeDTO) {
        log.info("向手机号为'{}'的用户发送验证码，其正在修改密码", userCodeDTO.getPhone());
        return Result.success(userService.sendEditCode(userCodeDTO));
    }

    /**
     * 用户修改密码
     *
     * @param userEditPasswordDTO 密码修改DTO
     * @return Result<LoginResult>
     */
    @PostMapping("/edit")
    @Operation(summary = "用户修改密码")
    public LoginResult editPassword(@RequestBody UserEditPasswordDTO userEditPasswordDTO) {
        log.info("用户'{}'正在修改密码", ThreadLocalUtil.getCurrentId());
        return userService.editPassword(userEditPasswordDTO);
    }

    /**
     * 用户退出
     *
     * @return Result<Void>
     */
    @PostMapping("/layout")
    @Operation(summary = "用户登出")
    public Result<Void> layout(@RequestBody UserLayoutDTO userLayoutDTO) {
        log.info("用户'{}'退出登录", ThreadLocalUtil.getCurrentId());
        userService.layout(userLayoutDTO);
        return Result.success();
    }

    /**
     * 用户个人主页
     *
     * @return Result<UserVO>
     */
    @PostMapping("/me")
    @Operation(summary = "用户个人页面")
    public Result<UserVO> me() {
        Long userId = ThreadLocalUtil.getCurrentId();
        log.info("用户'{}'正在查看个人页面", userId);
        return Result.success(userService.selectById(userId));
    }

    /**
     * 展示用户收藏
     *
     * @return Result<List<BriefSchoolInfoVO>>
     */
    @PostMapping("/favorite")
    @Operation(summary = "展示用户收藏")
    public Result<List<BriefSchoolInfoVO>> showFavorite() {
        log.info("展示用户'{}'收藏", ThreadLocalUtil.getCurrentId());
        return Result.success(userService.showFavorite());
    }

    /**
     * 用户修改信息
     *
     * @param userDTO 用户DTO
     * @return Result<Void>
     */
    @PostMapping("/update")
    @Operation(summary = "修改用户信息")
    public Result<Void> updateUser(@RequestBody UserDTO userDTO) {
        log.info("用户'{}'正在修改信息...", userDTO.getId());
        userService.update(userDTO);
        return Result.success();
    }

    /**
     * 用户注销发送验证码
     *
     * @param userCodeDTO 验证码DTO
     * @return code 验证码
     */
    @PostMapping("/deleteCode")
    @Operation(summary = "用户注销发送验证码")
    public Result<String> sendDeleteCode(@RequestBody UserCodeDTO userCodeDTO) {
        log.info("用户'{}'正在进行注销操作", ThreadLocalUtil.getCurrentId());
        return Result.success(userService.sendDeleteCode(userCodeDTO));
    }

    /**
     * 用户注销
     *
     * @param userDeleteDTO 用户注销DTO
     */
    @PostMapping("/delete")
    @Operation(summary = "用户注销")
    public Result<Void> deleteUser(@RequestBody UserDeleteDTO userDeleteDTO) {
        log.info("手机号为'{}'的用户正在注销", userDeleteDTO.getPhone());
        userService.deleteUser(userDeleteDTO);
        return Result.success();
    }

    /**
     * 查看用户评论
     *
     * @return Result<List<String>> 评论列表
     */
    @PostMapping("/comment")
    @Operation(summary = "用户评论")
    public Result<List<String>> showComment() {
        log.info("用户'{}'正在查看评论", ThreadLocalUtil.getCurrentId());
        return Result.success(userService.showComment());
    }

}
