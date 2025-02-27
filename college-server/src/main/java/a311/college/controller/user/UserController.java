package a311.college.controller.user;

import a311.college.constant.API.APIConstant;
import a311.college.constant.JWT.JWTClaimsConstant;
import a311.college.constant.user.UserStatusConstant;
import a311.college.dto.PasswordEditDTO;
import a311.college.dto.UserDTO;
import a311.college.dto.UserLoginDTO;
import a311.college.dto.UserPageQueryDTO;
import a311.college.entity.User;
import a311.college.jwt.JWTUtils;
import a311.college.properties.JWTProperties;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.service.UserService;
import a311.college.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户请求控制器
 */
@Slf4j
@RestController
@RequestMapping("/users")
@Tag(name = APIConstant.USER_SERVICE)
public class UserController {

    private final UserService userService;

    private final JWTProperties jwtProperties;

    @Autowired
    public UserController(UserService userService, JWTProperties jwtProperties) {
        this.userService = userService;
        this.jwtProperties = jwtProperties;
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 封装用户登录数据的DTO
     * @return 用户登录结果VO
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户{}正在登录", userLoginDTO.getUsername());
        User user = userService.login(userLoginDTO);
        // 登录成功之后，生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWTClaimsConstant.USER_ID, user.getId());
        claims.put(JWTClaimsConstant.USERNAME, user.getUsername());
        String token = JWTUtils.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTime(),
                claims);
        // 封装VO对象
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .token(token)
                .build();
        return Result.success(userLoginVO);
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
