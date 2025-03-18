package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 用户名查重DTO
 */
@Data
@Schema(description = "用户名查重DTO")
public class UserCheckUsernameDTO {

    @Schema(description = "用户名")
    private String username;
}
