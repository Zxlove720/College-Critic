package a311.college.vo.school;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 学校分数线VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "学校分数线VO")
public class SchoolYearScoreVO implements Serializable {

    @Schema(description = "招生年份")
    private String year;

    @Schema(description = "招生批次")
    private String batchName;

    @Schema(description = "最低分数")
    private String minScore;

    @Schema(description = "最低位次")
    private String minRanking;

    @Schema(description = "专业名")
    private String majorName;

    @Schema(description = "首选科目要求")
    private String firstChoice;

}
