package a311.college.entity.ai;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户请求AI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户请求AI")
public class UserAIRequest implements Serializable {

    @Schema(description = "请求消息")
    private UserAIRequestMessage message;

    @Schema(description = "模型温度")
    private Double temperature;

    @Schema(description = "是否启用流式编程")
    private Boolean stream;

}
