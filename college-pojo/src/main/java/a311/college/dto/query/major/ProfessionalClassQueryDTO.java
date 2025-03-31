package a311.college.dto.query.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 专业类别查询DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "专业类别查询DTO")
public class ProfessionalClassQueryDTO {

    @Schema(description = "学科门类id")
    private Integer subjectCategoryId;

}
