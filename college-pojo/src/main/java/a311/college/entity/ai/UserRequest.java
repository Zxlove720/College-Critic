package a311.college.entity.ai;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户AI请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest implements Serializable {

    @Schema(description = "请求消息")
    private Message message;

    @Schema(description = "模型温度")
    private Double temperature;

    @Schema(description = "是否启用流式编程")
    private Boolean stream;

}
