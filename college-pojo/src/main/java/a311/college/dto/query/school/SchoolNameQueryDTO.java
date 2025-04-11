package a311.college.dto.query.school;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 根据学校名搜索大学DTO
 */
@Data
@Schema(description = "根据学校名搜索大学DTO")
public class SchoolNameQueryDTO {

    @Schema(description = "学校名")
    private String schoolName;

}
