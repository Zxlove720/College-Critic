package a311.college.vo.ai;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * 用户请求AI消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户AI请求消息")
public class UserAIRequestMessageVO {

    @Schema(description = "对话角色")
    private String role;

    @Schema(description = "消息内容")
    private String content;

}
