package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "验证码DTO")
public class CodeDTO {

    @Schema(description = "手机号")
    private String phone;
}
