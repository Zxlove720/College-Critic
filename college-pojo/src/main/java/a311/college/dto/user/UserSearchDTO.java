package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户搜索DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户搜索DTO")
public class UserSearchDTO {

    @Schema(description = "搜索内容")
    private String message;

}
