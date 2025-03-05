package a311.college.entity.ai;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * AI请求消息类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    @Schema(description = "角色")
    private String role;

    @Schema(description = "消息内容")
    private String content;

}
