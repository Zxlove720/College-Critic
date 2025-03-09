package a311.college.dto.query.school;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "据成绩查询DTO")
public class GradeDTO {

    @Schema(description = "用户成绩")
    private Integer grade;

    @Schema(description = "用户省份")
    private String province;
}
