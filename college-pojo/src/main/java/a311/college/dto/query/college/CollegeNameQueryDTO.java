package a311.college.dto.query.college;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 根据学校名查询大学DTO
 */
@Data
@Schema(description = "根据学校名查询大学DTO")
public class CollegeNameQueryDTO {

    @Schema(description = "学校名")
    private String collegeName;
}
