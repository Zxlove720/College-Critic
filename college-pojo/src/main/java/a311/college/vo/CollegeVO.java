package a311.college.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * College View Object (VO) for holding the result of a multi-table query.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollegeVO implements Serializable {

    @Schema(description = "大学名称")
    private String schoolName;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "年份")
    private Integer year; // Assuming 'year' is an integer

    @Schema(description = "招生类别名称")
    private String category;

    @Schema(description = "批次")
    private String batch;

    @Schema(description = "专业")
    private String major;

    @Schema(description = "最低分数")
    private String minScore; // Assuming 'min_score' is an integer

}