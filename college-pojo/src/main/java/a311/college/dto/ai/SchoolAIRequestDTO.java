package a311.college.dto.ai;

import a311.college.vo.ai.SchoolAIRequestMessageVO;
import io.swagger.v3.oas.annotations.media.Schema;

public class SchoolAIRequestDTO {

    @Schema(description = "请求消息")
    private SchoolAIRequestMessageVO message;
}
