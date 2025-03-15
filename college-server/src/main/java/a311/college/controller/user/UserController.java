package a311.college.controller.user;

import a311.college.constant.API.APIConstant;
import a311.college.dto.user.*;
import a311.college.dto.login.LoginDTO;
import a311.college.result.LoginResult;
import a311.college.result.Result;
import a311.college.service.UserService;
import a311.college.thread.ThreadLocalUtil;
import a311.college.vo.CollegeSimpleVO;
import a311.college.vo.UserVO;
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
     * @param loginDTO 用户登录数据DTO
     * @return Result<String>
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<LoginResult> login(@RequestBody LoginDTO loginDTO) {
        String phone = loginDTO.getPhone();
        log.info("手机号为'{}'的用户正在登录", phone);
        return Result.success(userService.login(loginDTO));
    }

    /**
     * 用户发送验证码注册
     *
     * @param codeDTO 验证码DTO
     * @return code 验证码
     */
    @PostMapping("/registerCode")
    @Operation(summary = "用户发送验证码注册")
    public Result<String> sendRegisterCode(@RequestBody CodeDTO codeDTO) {
        log.info("向手机号为'{}'的用户发送验证码，其正在注册", codeDTO.getPhone());
        return Result.success(userService.sendRegisterCode(codeDTO));
    }

    /**
     * 用户名检查
     *
     * @param checkUserDTO 用户名检查DTO
     * @return Integer 0该用户名不可用 1该用户名可用
     */
    @PostMapping("/check")
    @Operation(summary = "用户名检查")
    public Result<Integer> checkUser(@RequestBody CheckUserDTO checkUserDTO) {
        return Result.success(userService.checkUser(checkUserDTO));
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
     * @param codeDTO 验证码DTO
     * @return Result<String>
     */
    @PostMapping("/editCode")
    @Operation(summary = "用户发送验证码修改密码")
    public Result<String> sendEditCode(@RequestBody CodeDTO codeDTO) {
        log.info("向手机号为'{}'的用户发送验证码，其正在修改密码", codeDTO.getPhone());
        return Result.success(userService.sendEditCode(codeDTO));
    }

    /**
     * 用户修改密码
     *
     * @param passwordEditDTO 密码修改DTO
     * @return Result<LoginResult>
     */
    @PostMapping("/edit")
    @Operation(summary = "用户修改密码")
    public LoginResult editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("用户'{}'正在修改密码", ThreadLocalUtil.getCurrentId());
        return userService.editPassword(passwordEditDTO);
    }

    /**
     * 用户退出
     *
     * @return Result<Void>
     */
    @PostMapping("/layout")
    @Operation(summary = "用户登出")
    public Result<Void> layout(@RequestBody LayoutDTO layoutDTO) {
        log.info("用户'{}'退出登录", ThreadLocalUtil.getCurrentId());
        userService.layout(layoutDTO);
        return Result.success();
    }

    /**
     * 用户个人主页
     *
     * @return Result
     */
    @PostMapping("/me")
    @Operation(summary = "用户个人页面")
    public Result<UserVO> me() {
        Long userId = ThreadLocalUtil.getCurrentId();
        log.info("用户'{}'正在查看个人页面", userId);
        return Result.success(userService.selectById(userId));
    }

    /**
     * 用户收藏学校
     *
     * @param addFavoriteDTO 学校收藏DTO
     * @return Void
     */
    @PostMapping("/addFavorite")
    @Operation(summary = "用户收藏学校")
    public Result<Void> addFavorite(@RequestBody AddFavoriteDTO addFavoriteDTO) {
        log.info("用户'{}'收藏了'{}'学校", ThreadLocalUtil.getCurrentId(), addFavoriteDTO.getSchoolId());
        userService.addFavorite(addFavoriteDTO);
        return Result.success();
    }

    /**
     * 展示用户收藏
     *
     * @return Result<Void>
     */
    @PostMapping("/favorite")
    @Operation(summary = "展示用户收藏")
    public Result<List<CollegeSimpleVO>> showFavorite() {
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
     * @param codeDTO 验证码DTO
     * @return code 验证码
     */
    @PostMapping("/deleteCode")
    @Operation(summary = "用户注销发送验证码")
    public Result<String> sendDeleteCode(@RequestBody CodeDTO codeDTO) {
        log.info("用户'{}'正在进行注销操作", ThreadLocalUtil.getCurrentId());
        return Result.success(userService.sendDeleteCode(codeDTO));
    }

    /**
     * 用户注销
     *
     * @param deleteDTO 用户注销DTO
     */
    @PostMapping("/delete")
    @Operation(summary = "用户注销")
    public Result<Void> deleteUser(@RequestBody DeleteDTO deleteDTO) {
        log.info("手机号为'{}'的用户正在注销", deleteDTO.getPhone());
        userService.deleteUser(deleteDTO);
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
