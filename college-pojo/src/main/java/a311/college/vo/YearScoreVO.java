package a311.college.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 历年分数线VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "历年分数线VO")
public class YearScoreVO implements Serializable {

    @Schema(description = "招生年份")
    private String year;

    @Schema(description = "招生批次")
    private String batchName;

    @Schema(description = "最低分数")
    private String minScore;

    @Schema(description = "最低位次")
    private String minRanking;

    @Schema(description = "选科要求")
    private String majorName;


}
