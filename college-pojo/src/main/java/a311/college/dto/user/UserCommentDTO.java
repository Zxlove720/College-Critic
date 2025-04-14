package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户评论DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户评论DTO")
public class UserCommentDTO {

    @Schema(description = "评论id")
    private Integer commentId;

}
