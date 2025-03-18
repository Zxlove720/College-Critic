package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户注销DTO
 */
@Data
@Schema(description = "用户注销DTO")
public class UserDeleteDTO {

    @Schema(description = "用户手机号")
    private String phone;

    @Schema(description = "验证码")
    private String code;
}
