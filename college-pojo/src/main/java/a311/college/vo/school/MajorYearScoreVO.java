package a311.college.vo.school;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 专业分数线VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "专业分数线VO")
public class MajorYearScoreVO {

    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "首选科目")
    private String firstChoice;

    @Schema(description = "次选科目")
    private String otherChoice;

    @Schema(description = "特殊要求")
    private String special;

    @Schema(description = "批次名称")
    private String batchName;

    @Schema(description = "最低分")
    private Integer minScore;

    @Schema(description = "最低位次")
    private Integer minRanking;

}
