package a311.college.controller.user;

import a311.college.constant.API.APIConstant;
import a311.college.dto.login.PhoneLoginDTO;
import a311.college.dto.user.AddFavoriteDTO;
import a311.college.dto.user.UserDTO;
import a311.college.dto.login.LoginDTO;
import a311.college.result.LoginResult;
import a311.college.result.Result;
import a311.college.service.UserService;
import a311.college.thread.ThreadLocalUtil;
import a311.college.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
     * 普通登录
     *
     * @param loginDTO 封装用户登录数据的DTO
     * @return Result<String>
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<LoginResult> login(@RequestBody LoginDTO loginDTO) {
        String phone = loginDTO.getPhone();
        log.info("用户{}正在登录", phone);
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
    @PostMapping("/phone")
    @Operation(summary = "手机登录")
    public Result<LoginResult> phoneLogin(@RequestBody PhoneLoginDTO phoneLoginDTO) {
        log.info("手机号为{}的用户正在使用验证码登录", phoneLoginDTO.getPhone());
        return Result.success(userService.phoneLogin(phoneLoginDTO));
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
     * 用户个人主页
     *
     * @return Result
     */
    @PostMapping("/me")
    @Operation(summary = "用户个人页面")
    public Result<UserVO> me() {
        // 获取当前登录用户id
        Long userId = ThreadLocalUtil.getCurrentId();
        return Result.success(userService.selectById(userId));
    }

    /**
     * 用户收藏学校
     *
     * @param addFavoriteDTO 学校收藏DTO
     * @return Void
     */
    public Result<Void> addFavorite(@RequestBody AddFavoriteDTO addFavoriteDTO) {
        log.info("用户{}收藏了{}学校", addFavoriteDTO.getUserId(), addFavoriteDTO.getSchoolId());
        userService.addFavorite(addFavoriteDTO);
        return Result.success();
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
    @PostMapping("/register")
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


}
