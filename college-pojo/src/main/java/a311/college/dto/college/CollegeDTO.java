package a311.college.dto.college;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 查询大学详细信息DTO
 */
@Data
@Schema(description = "查询大学详细信息DTO")
public class CollegeDTO {

    @Schema(description = "大学id")
    private String collegeId;
}
