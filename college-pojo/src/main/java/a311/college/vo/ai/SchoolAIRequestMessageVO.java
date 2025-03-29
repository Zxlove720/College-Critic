package a311.college.vo.ai;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "大学AI请求消息")
public class SchoolAIRequestMessageVO {

    @Schema(description = "对话角色")
    private String role;

    @Schema(description = "消息内容")
    private String content;
}
