package a311.college.dto.query.school;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学校评论区分页查询DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "学校评论区分页查询DTO")
public class SchoolCommentPageQueryDTO {

    @Schema(description = "学校id")
    private Integer schoolId;

    @Schema(description = "查询页码")
    private Integer page = 1;

    @Schema(description = "每页大小")
    private Integer pageSize = 10;

}
