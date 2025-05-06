package a311.college.dto.query.school;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论区查询DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "评论区查询DTO")
public class CommentDTO {

    @Schema(description = "学校id")
    private Integer schoolId;

    @Schema(description = "专业id")
    private Integer majorId;

}
