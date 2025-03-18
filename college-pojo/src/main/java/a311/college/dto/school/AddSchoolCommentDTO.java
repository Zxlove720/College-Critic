package a311.college.dto.school;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 添加学校评论DTO
 */
@Data
@Schema(description = "添加学校评论DTO")
public class AddSchoolCommentDTO {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "大学id")
    private Integer schoolId;

    @Schema(description = "评论内容")
    private String comment;
}
