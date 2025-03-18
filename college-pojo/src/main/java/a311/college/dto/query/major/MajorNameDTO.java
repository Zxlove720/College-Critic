package a311.college.dto.query.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 根据专业名查询专业DTO
 */
@Data
@Schema(description = "根据专业名查询专业DTO")
public class MajorNameDTO {

    @Schema(description = "专业名")
    private String majorName;
}
