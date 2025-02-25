package a311.college.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户登录VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "JWT令牌")
    private String token;

}
