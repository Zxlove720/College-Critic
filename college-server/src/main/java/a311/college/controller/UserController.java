package a311.college.controller;

import a311.college.constant.JWTClaimsConstant;
import a311.college.constant.UserStatusConstant;
import a311.college.dao.PasswordEditDTO;
import a311.college.dao.UserDTO;
import a311.college.dao.UserLoginDTO;
import a311.college.dao.UserPageQueryDTO;
import a311.college.entity.User;
import a311.college.jwt.JWTUtils;
import a311.college.properties.JWTProperties;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.service.UserService;
import a311.college.vo.UserLoginVO;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/user")
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
    @ApiOperation(value = "用户登录")
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
    @ApiOperation(value = "用户退出")
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
    @ApiOperation(value = "用户分页查询")
    public Result<PageResult<User>> UserPage(UserPageQueryDTO userPageQueryDTO) {
        log.info("用户分页查询，查询参数为：第{}页，每页{}条", userPageQueryDTO.getPage(), userPageQueryDTO.getPageSize());
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
    @ApiOperation(value = "根据id查询用户")
    public Result<User> selectUserById(@PathVariable Long id) {
        log.info("根据id查询用户。id{}", id);
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
    @ApiOperation(value = "根据id删除用户")
    public Result<Void> deleteById(@PathVariable Long id) {
        log.info("删除用户（用户注销），id：{}", id);
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
    @ApiOperation(value = "新增用户（用户注册）")
    public Result<Void> saveUser(@RequestBody UserDTO userDTO) {
        log.info("用户：{}，正在注册", userDTO);
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
    @ApiOperation(value = "修改用户信息")
    public Result<Void> updateUser(@RequestBody UserDTO userDTO) {
        log.info("用户：{}，正在修改信息", userDTO.getUsername());
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
    @ApiOperation(value = "修改用户状态")
    public Result<Void> changeStatus(@PathVariable Integer status, Long id) {
        if (status.equals(UserStatusConstant.ENABLE)) {
            log.info("禁用员工账号：{}，员工id为：{}", status, id);
        } else {
            log.info("启用员工账号：{}，员工id为：{}", status, id);
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
    @ApiOperation(value = "用户修改密码")
    public Result<Void> editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        userService.editPassword(passwordEditDTO);
        return Result.success();
    }
}
