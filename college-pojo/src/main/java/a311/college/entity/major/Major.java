package a311.college.entity.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 专业实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "专业实体类")
public class Major implements Serializable {

    @Schema(description = "学科门类")
    private String subjectCategoryName;

    @Schema(description = "专业类别")
    private String professionalClassName;
    
    @Schema(description = "专业id")
    private Integer majorId;

    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "专业代码")
    private String majorCode;

    @Schema(description = "修业年限")
    private String majorYear;

    @Schema(description = "授予学位")
    private String degrees;

    @Schema(description = "男女比例")
    private String gender;

    @Schema(description = "平均薪资")
    private Integer avgSalary;
}
