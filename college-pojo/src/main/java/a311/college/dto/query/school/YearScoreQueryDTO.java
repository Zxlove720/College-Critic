package a311.college.dto.query.school;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 院校分数线查询DTO
 */
@Data
@Schema(description = "院校分数线查询DTO")
public class YearScoreQueryDTO {

    @Schema(description = "学校id")
    private Integer schoolId;

    @Schema(description = "招生省份")
    private String province;

    @Schema(description = "招生年份")
    private Integer year;
}
