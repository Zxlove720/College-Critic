package a311.college.vo.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 专业详细信息VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "专业信息VO")
public class MajorVO implements Serializable {

    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "专业代码")
    private String majorCode;

    @Schema(description = "专业年限")
    private String majorYear;

    @Schema(description = "授予学位")
    private String degrees;

    @Schema(description = "男女比例")
    private String gender;

    @Schema(description = "平均薪资")
    private String avgSalary;
}
