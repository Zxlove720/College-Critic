package a311.college.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学科门类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectCategoryVO {

    @Schema(description = "学科门类id")
    private String id;

    @Schema(description = "学科门类名")
    private String name;
}
