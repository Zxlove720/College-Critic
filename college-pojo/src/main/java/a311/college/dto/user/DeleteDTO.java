package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户注销DTO")
public class DeleteDTO {

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "验证码")
    private String code;
}
