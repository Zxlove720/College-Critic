package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户修改密码（原始）DTO")
public class PasswordEditDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "旧密码")
    private String oldPassword;

    @Schema(description = "新密码")
    private String newPassword;

}
