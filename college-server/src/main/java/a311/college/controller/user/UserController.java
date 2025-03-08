package a311.college.controller.user;

import a311.college.constant.API.APIConstant;
import a311.college.constant.user.UserStatusConstant;
import a311.college.dto.user.PasswordEditDTO;
import a311.college.dto.login.PhoneLoginDTO;
import a311.college.dto.user.UserDTO;
import a311.college.dto.login.LoginDTO;
import a311.college.dto.user.UserPageQueryDTO;
import a311.college.entity.user.User;
import a311.college.properties.JWTProperties;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 用户普通登录
     *
     * @param loginDTO 封装用户登录数据的DTO
     * @return Result<String>
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<String> login(@RequestBody LoginDTO loginDTO, HttpServletResponse httpServletResponse) {
        String username = loginDTO.getUsername();
        log.info("用户{}正在登录", username);
        return Result.success(userService.login(loginDTO));
    }

    /**
     * 发送手机验证码
     *
     * @param phone 手机号
     * @return Result<String>
     */
    @PostMapping("/code")
    @Operation(summary = "发送验证码")
    public Result<String> sendCode(String phone) {
        log.info("向手机号为{}的用户发送验证码", phone);
        String code = userService.sendCode(phone);
        return Result.success(code);
    }

    /**
     * 手机登录
     *
     * @param phoneLoginDTO 手机登录DTO
     * @return Result<String>
     */
    @PostMapping("/login/phone")
    @Operation(summary = "手机登录")
    public Result<String> phoneLogin(PhoneLoginDTO phoneLoginDTO) {
        log.info("手机号为{}的用户正在登录", phoneLoginDTO.getPhone());
        String token = userService.phoneLogin(phoneLoginDTO);
        return Result.success(token);
    }

    /**
     * 用户退出
     *
     * @return Result<Void>
     */
    @PostMapping("/layout")
    @Operation(summary = "用户登出")
    public Result<Void> layout() {
        log.info("用户退出登录");
        return Result.success();
    }

    /**
     * 用户分页查询
     *
     * @param userPageQueryDTO 用户分页查询DTO
     * @return Result<PageResult < User>>
     */
    @GetMapping("/page")
    @Operation(summary = "用户分页查询")
    public Result<PageResult<User>> UserPage(UserPageQueryDTO userPageQueryDTO) {
        log.info("用户分页查询...查询参数为：第{}页，每页{}条", userPageQueryDTO.getPage(), userPageQueryDTO.getPageSize());
        PageResult<User> pageResult = userService.pageSelect(userPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return Result<User>
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询用户")
    public Result<User> selectUserById(@PathVariable Long id) {
        log.info("根据id查询用户...id为：{}", id);
        User user = userService.selectById(id);
        return Result.success(user);
    }

    /**
     * 删除用户（用户注销）
     *
     * @param id 用户id
     * @return Result<Void>
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "根据id删除用户")
    public Result<Void> deleteById(@PathVariable Long id) {
        log.info("删除用户（用户注销）...id为{}", id);
        userService.deleteById(id);
        return Result.success();
    }

    /**
     * 新增用户（用户注册）
     *
     * @param userDTO 用户DTO
     * @return Result<Void>
     */
    @PostMapping
    @Operation(summary = "新增用户")
    public Result<Void> saveUser(@RequestBody UserDTO userDTO) {
        log.info("用户：{}，正在注册...", userDTO);
        userService.save(userDTO);
        return Result.success();
    }

    /**
     * 修改用户信息
     *
     * @param userDTO 用户DTO
     * @return Result<Void>
     */
    @PutMapping
    @Operation(summary = "修改用户信息")
    public Result<Void> updateUser(@RequestBody UserDTO userDTO) {
        log.info("用户：{}，正在修改信息...", userDTO.getUsername());
        userService.update(userDTO);
        return Result.success();
    }

    /**
     * 修改用户状态
     *
     * @param status 用户状态 前端传递的是将用户当前的状态
     * @param id     用户id
     * @return Result<Void>
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "修改用户状态")
    public Result<Void> changeStatus(@PathVariable Integer status, Long id) {
        if (status.equals(UserStatusConstant.ENABLE)) {
            log.info("启用用户账号：用户id为：{}", id);
        } else {
            log.info("禁用用户账号：用户id为：{}", id);
        }
        userService.changeStatus(status, id);
        return Result.success();
    }

    /**
     * 用户修改密码
     *
     * @param passwordEditDTO 用户密码修改DTO
     * @return Result<Void>
     */
    @PutMapping("/editPassword")
    @Operation(summary = "用户修改密码")
    public Result<Void> editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        userService.editPassword(passwordEditDTO);
        return Result.success();
    }
}
