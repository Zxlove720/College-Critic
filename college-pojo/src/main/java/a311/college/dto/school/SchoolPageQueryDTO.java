package a311.college.dto.school;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 大学分页查询DTO
 */
@Data
@Schema(description = "大学分页查询DTO")
public class SchoolPageQueryDTO {

    @Schema(description = "大学名")
    private String schoolName;

    @Schema(description = "省份名")
    private String province;

    @Schema(description = "大学等级（类型）")
    private List<String> rankList;

    @Schema(description = "最低分")
    private Integer minScore;

    @Schema(description = "最高分")
    private Integer maxScore;

    @Schema(description = "招生年份")
    private Integer year;

    @Schema(description = "首选科目")
    private String firstChoice;

    @Schema(description = "查询页码")
    private Integer page = 1;

    @Schema(description = "每页大小")
    private Integer pageSize = 15;
}
