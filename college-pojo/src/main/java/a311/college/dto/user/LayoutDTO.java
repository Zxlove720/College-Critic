package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登出DTO")
public class LayoutDTO {

    @Schema(description = "手机号")
    private String phone;
}
