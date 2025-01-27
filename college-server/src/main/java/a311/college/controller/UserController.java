package a311.college.controller;

import a311.college.constant.JWTClaimsConstant;
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
    public UserController (UserService userService, JWTProperties jwtProperties) {
        this.userService = userService;
        this.jwtProperties = jwtProperties;
    }

    /**
     * 用户登录
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
     * @param userPageQueryDTO 用户分页查询DTO
     * @return Result<PageResult<User>>
     */
    @GetMapping("/page")
    @ApiOperation(value = "用户分页查询")
    public Result<PageResult<User>> UserPage(UserPageQueryDTO userPageQueryDTO) {
        log.info("用户分页查询，查询参数为：{}", userPageQueryDTO);
        PageResult<User> pageResult = userService.pageSelect(userPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询用户
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

}
