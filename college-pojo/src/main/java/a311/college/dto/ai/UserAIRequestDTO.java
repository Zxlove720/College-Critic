package a311.college.dto.ai;

import a311.college.vo.ai.UserAIMessageVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 用户请求AI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户请求AI")
public class UserAIRequestDTO {

    @Schema(description = "请求消息")
    private UserAIMessageVO message;

    @Schema(description = "模型温度")
    private Double temperature;

}
