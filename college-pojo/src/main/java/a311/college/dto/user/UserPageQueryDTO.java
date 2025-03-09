package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "用户分页查询DTO")
public class UserPageQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "页码")
    private Integer page;

    @Schema(description = "每页大小")
    private Integer pageSize;
}
