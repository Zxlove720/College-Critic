package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户收藏专业DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户收藏专业DTO")
public class UserAddFavoriteMajorDTO {

    @Schema(description = "专业id")
    private String MajorId;
}
