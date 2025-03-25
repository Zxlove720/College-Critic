package a311.college.vo.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 专业简略信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "专业简略信息")
public class BriefMajorVO {

    @Schema(description = "专业名")
    private String majorName;

    @Schema(description = "男女比例")
    private String gender;

    @Schema(description = "平均薪资")
    private String avgSalary;

    @Schema(description = "专业信息")
    private String information;
}
