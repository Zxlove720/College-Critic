package a311.college.dto.query.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 专业查询DTO
 */
@Data
@Schema(description = "专业查询DTO")
public class MajorPageQueryDTO {

    @Schema(description = "专业所属层次id")
    private Integer academicLevelId;

    @Schema(description = "专业所属分类id")
    private Integer subjectCategoryId;

    @Schema(description = "专业所属专业类id")
    private Integer professionalClassId;

    @Schema(description = "查询页码")
    private Integer page = 1;

    @Schema(description = "每页大小")
    private Integer pageSize = 15;
}
