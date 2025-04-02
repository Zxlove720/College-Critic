package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 用户收藏大学DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户收藏大学DTO")
public class UserAddFavoriteSchoolDTO {

    @Schema(description = "大学id")
    private String schoolId;

}
