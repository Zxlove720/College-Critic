package a311.college.dto.school;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 添加学校评论DTO
 */
@Data
@Schema(description = "添加学校评论DTO")
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentDTO {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "大学id")
    private Integer schoolId;

    @Schema(description = "大学名称")
    private String schoolName;

    @Schema(description = "专业id")
    private Integer majorId;

    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "评论内容")
    private String comment;

    @Schema(description = "发表时间")
    private LocalDateTime addTime;
}
