package a311.college.entity.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学科门类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectCategory implements Serializable {

    @Schema(description = "专业大类id")
    private Integer subjectCategoryId;

    @Schema(description = "专业大类名")
    private String subjectCategoryName;

}
