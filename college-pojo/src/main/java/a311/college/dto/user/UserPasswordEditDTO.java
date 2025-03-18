package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 用户修改密码DTO
 */
@Data
@Schema(description = "用户修改密码DTO")
public class UserPasswordEditDTO {

    @Schema(description = "用户手机号")
    private String phone;

    @Schema(description = "验证码")
    private String code;

    @Schema(description = "新密码")
    private String newPassword;

}
