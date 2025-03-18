package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 发送验证码DTO
 */
@Data
@Schema(description = "发送验证码DTO")
public class UserCodeDTO {

    @Schema(description = "用户手机号")
    private String phone;
}
