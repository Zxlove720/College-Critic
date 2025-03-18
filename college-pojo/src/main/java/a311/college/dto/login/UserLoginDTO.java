package a311.college.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录DTO
 */
@Data
@Schema(description = "用户登录DTO")
public class UserLoginDTO implements Serializable {

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "用户密码")
    private String password;

}
