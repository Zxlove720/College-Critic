package a311.college.dto.query.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 开设某专业学校分页查询DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "开设某专业学校分页查询DTO")
public class MajorSchoolPageQueryDTO {

    @Schema(description = "专业id")
    private Integer majorId;

    @Schema(description = "省份名")
    private String province;

    @Schema(description = "大学等级（类型）")
    private List<String> rankList;

    @Schema(description = "查询页码")
    private Integer page = 1;

    @Schema(description = "每页大小")
    private Integer pageSize = 15;
}
