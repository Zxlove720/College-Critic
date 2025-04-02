package a311.college.dto.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 专业DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorDTO {

    @Schema(description = "专业id")
    private Integer majorId;

    @Schema(description = "用户id")
    private Long userId;

}
