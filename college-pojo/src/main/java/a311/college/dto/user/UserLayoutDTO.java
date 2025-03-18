package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户登出DTO
 */
@Data
@Schema(description = "用户登出DTO")
public class UserLayoutDTO {

    @Schema(description = "用户手机号")
    private String phone;
}
