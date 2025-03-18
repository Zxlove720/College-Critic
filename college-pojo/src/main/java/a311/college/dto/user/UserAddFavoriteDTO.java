package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 用户收藏大学DTO
 */
@Data
@Schema(description = "用户收藏大学DTO")
public class UserAddFavoriteDTO {

    @Schema(description = "大学id")
    private String schoolId;

}
