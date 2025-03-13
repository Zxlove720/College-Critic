package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "学校收藏DTO")
public class AddFavoriteDTO {

    @Schema(description = "学校id")
    private String schoolId;

}
