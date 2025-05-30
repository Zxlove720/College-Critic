package a311.college.dto.school;

import a311.college.enumeration.ProvinceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "录取预测DTO")
public class ForecastDTO {

    @Schema(description = "学校id")
    private Integer schoolId;

    @Schema(description = "专业id")
    private Integer majorId;

    @Schema(description = "用户所在省份")
    private ProvinceEnum province;

    @Schema(description = "首选科目")
    private String subject;

    @Schema(description = "用户位次")
    private Integer ranking;

    @Schema(description = "用户成绩")
    private Integer grade;
}
