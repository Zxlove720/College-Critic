package a311.college.entity.ai;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 用户请求AI消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户请求AI消息")
public class UserRequestAIMessage implements Serializable {

    @Schema(description = "对话角色")
    private String role;

    @Schema(description = "消息内容")
    private String content;

}
