package a311.college.dto.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 专业查询DTO
 */
@Data
@Schema(description = "专业查询DTO")
public class MajorQueryDTO {

    @Schema(description = "专业所属层次id")
    private Integer levelId;

    @Schema(description = "专业所属分类id")
    private Integer categoryId;

    @Schema(description = "专业所属专业类id")
    private Integer professionalId;
}
