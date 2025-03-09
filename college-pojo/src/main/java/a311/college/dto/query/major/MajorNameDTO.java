package a311.college.dto.query.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "专业名DTO")
public class MajorNameDTO {

    @Schema(description = "专业名")
    private String majorName;
}
