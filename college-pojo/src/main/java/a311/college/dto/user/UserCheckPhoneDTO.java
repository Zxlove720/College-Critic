package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 用户手机号查重DTO
 */
@Data
@Schema(description = "用户手机号查重DTO")
public class UserCheckPhoneDTO {

    @Schema(description = "用户手机号")
    private String phone;
}
