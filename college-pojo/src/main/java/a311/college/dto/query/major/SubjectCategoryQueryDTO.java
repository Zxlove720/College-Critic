package a311.college.dto.query.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学科门类查询DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectCategoryQueryDTO {

    @Schema(description = "培养层次id")
    private Integer academicId;
}
