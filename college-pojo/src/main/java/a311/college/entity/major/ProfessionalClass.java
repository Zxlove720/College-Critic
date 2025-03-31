package a311.college.entity.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 专业类别实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "专业类别实体类")
public class ProfessionalClass implements Serializable {

    @Schema(description = "专业类别id")
    private Integer professionalClassId;

    @Schema(description = "所属学科门类id")
    private Integer subjectCategoryId;

    @Schema(description = "专业类别名称")
    private String professionalClassName;
}
