package a311.college.dto.volunteer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 添加志愿
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "添加志愿")
public class AddVolunteerDTO {

    @Schema(description = "专业id")
    private Integer majorId;

    @Schema(description = "所属类型")
    private Integer category;

    @Schema(description = "志愿数")
    private Integer count;

    @Schema(description = "招生年份")
    private Integer year;

    @Schema(description = "最低分数")
    private Integer minScore;

    @Schema(description = "最低位次")
    private Integer minRanking;

    @Schema(description = "最低分与用户相比")
    private Integer scoreThanMe;

    @Schema(description = "最低位次与用户相比")
    private Integer rankingThanMe;

}
