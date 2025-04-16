package a311.college.dto.volunteer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 添加志愿
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "添加志愿")
public class AddVolunteerDTO {

    @Schema(description = "专业id")
    private Integer majorId;

}
