package a311.college.dto.query.school;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "学校名查询DTO")
public class SchoolNameDTO {

    @Schema(description = "学校名")
    private String schoolName;
}
