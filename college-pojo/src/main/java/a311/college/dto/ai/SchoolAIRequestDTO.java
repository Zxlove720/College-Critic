package a311.college.dto.ai;

import a311.college.vo.ai.SchoolAIRequestMessageVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "大学AI请求")
public class SchoolAIRequestDTO {

    @Schema(description = "学校ID")
    private Integer schoolId;

    @Schema(description = "请求消息")
    private SchoolAIRequestMessageVO message;

    @Schema(description = "模型温度")
    private Double temperature;

    @Schema(description = "是否启用流式编程")
    private Boolean stream;
}
