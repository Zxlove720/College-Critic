package a311.college.entity.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 学科门类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectCategory implements Serializable {

    @Schema(description = "学科门类id")
    private Integer subjectCategoryId;

    @Schema(description = "学科门类名")
    private String subjectCategoryName;

    @Schema(description = "专业类别")
    private List<ProfessionalClass> professionalClassList;

}
