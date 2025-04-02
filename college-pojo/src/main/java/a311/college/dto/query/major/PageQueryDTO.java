package a311.college.dto.query.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页查询DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页查询DTO")
public class PageQueryDTO {

    @Schema(description = "查询页码")
    private Integer page;

    @Schema(description = "每页大小")
    private Integer pageSize;
}
