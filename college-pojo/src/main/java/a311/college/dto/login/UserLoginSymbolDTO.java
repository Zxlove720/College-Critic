package a311.college.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录凭证DTO
 */
@Data
@Schema(description = "用户登录凭证DTO")
public class UserLoginSymbolDTO implements Serializable {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户头像")
    private String head;

}
