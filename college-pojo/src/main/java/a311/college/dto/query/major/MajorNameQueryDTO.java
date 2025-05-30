package a311.college.dto.query.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 专业名称查询DTO
 */
@Data
@Schema(description = "根据专业名查询专业DTO")
public class MajorNameQueryDTO {

    @Schema(description = "专业名称")
    private String majorName;

}
