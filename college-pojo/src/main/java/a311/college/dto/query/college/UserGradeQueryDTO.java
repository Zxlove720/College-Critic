package a311.college.dto.query.college;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 根据用户成绩查询大学DTO
 */
@Data
@Schema(description = "根据用户成绩查询大学DTO")
public class UserGradeQueryDTO {

    @Schema(description = "用户成绩")
    private Integer grade;

    @Schema(description = "用户省份")
    private String province;
}
