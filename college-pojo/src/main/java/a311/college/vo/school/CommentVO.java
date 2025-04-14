package a311.college.vo.school;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 大学评论
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "大学评论")
public class CommentVO {

    @Schema(description = "评论id")
    private Integer commentId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户头像")
    private String head;

    @Schema(description = "学校id")
    private Integer schoolId;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "专业id")
    private Integer majorId;

    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "用户评论")
    private String comment;

    @Schema(description = "发表时间")
    private LocalDateTime time;
}
