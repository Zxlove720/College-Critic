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

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户头像")
    private String head;

    @Schema(description = "用户评论")
    private String comment;

    @Schema(description = "发表时间")
    private LocalDateTime time;
}
