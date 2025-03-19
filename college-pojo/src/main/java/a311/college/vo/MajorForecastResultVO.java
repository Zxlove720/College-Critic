package a311.college.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 专业预测返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "专业预测结果")
public class MajorForecastResultVO {

    @Schema(description = "专业名")
    private String majorName;

    @Schema(description = "难度分类")
    private Integer category;

    @Schema(description = "特殊要求")
    private String special;

    @Schema(description = "选科要求")
    private String require;

    @Schema(description = "最低分数")
    private Integer minScore;

    @Schema(description = "最低位次")
    private Integer minRanking;

}
