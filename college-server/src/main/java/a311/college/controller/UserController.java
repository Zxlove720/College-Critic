package a311.college.controller;

import a311.college.constant.JWTClaimsConstant;
import a311.college.dao.UserLoginDTO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户请求控制器
 */
@Slf4j
@RestController
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
    @ApiOperation(value = "员工登录")
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
     * 员工推出
     * @return Result统一返回结果
     */
    @PostMapping("/layout")
    @ApiOperation(value = "用户退出")
    public Result<Void> layout() {
        log.info("用户退出登录");
        return Result.success();
    }



}
