package a311.college.entity.ai;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI请求消息")
public class Message implements Serializable {

    @Schema(description = "对话角色")
    private String role;

    @Schema(description = "消息内容")
    private String content;

}
