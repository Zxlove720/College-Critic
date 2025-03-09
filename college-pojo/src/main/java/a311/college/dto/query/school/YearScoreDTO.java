package a311.college.dto.query.school;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "院校分数线查询DTO")
public class YearScoreDTO {

    @Schema(description = "学校id")
    private Integer id;

    @Schema(description = "招生省份")
    private String province;

    @Schema(description = "招生年份")
    private String year;
}
