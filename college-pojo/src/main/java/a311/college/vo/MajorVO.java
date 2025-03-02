package a311.college.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 专业响应VO类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorVO implements Serializable {

    @Schema(description = "平均薪资")
    private String name;

    @Schema(description = "专业代码")
    private String code;

    @Schema(description = "修业年限")
    private String year;

    @Schema(description = "授予学位")
    private String degrees;

    @Schema(description = "男女比例")
    private String gender;

    @Schema(description = "平均薪资")
    private String avgSalary;
}
