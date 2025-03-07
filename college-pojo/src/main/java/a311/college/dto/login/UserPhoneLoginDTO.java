package a311.college.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录（手机号）DTO
 */
@Data
public class UserPhoneLoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "验证码")
    private String code;

}
