package a311.college.dto.school;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 大学专业分页查询DTO
 */
@Data
public class SchoolMajorPageQueryDTO {

    @Schema(description = "大学id")
    private Integer schoolId;

    @Schema(description = "首选科目")
    private String firstChoice;

    @Schema(description = "查询页码")
    private Integer page;

    @Schema(description = "每页大小")
    private Integer pageSize;

}
