package a311.college.vo.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 专业详细信息VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "专业详细信息VO")
public class DetailMajorVO {

    @Schema(description = "学科门类")
    private String subjectCategoryName;

    @Schema(description = "专业类别")
    private String professionalClassName;

    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "专业代码")
    private String majorCode;

    @Schema(description = "修业年限")
    private String majorYear;

    @Schema(description = "授予学位")
    private String degrees;

    @Schema(description = "平均薪资")
    private Integer avgSalary;

    @Schema(description = "专业满意度")
    private List<Double> satisfaction;

    @Schema(description = "就业率")
    private List<String> employmentRate;

    @Schema(description = "男女比例")
    private String gender;

    @Schema(description = "是否被收藏")
    private Boolean favorite;

}
