package a311.college.dto.college;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "大学分页查询DTO")
public class CollegePageQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "学校名")
    private String schoolName;

    @Schema(description = "省份名")
    private String province;

    @Schema(description = "成绩")
    private Integer grade;

    @Schema(description = "大学等级（类型）")
    private List<String> rankList;

    @Schema(description = "页码")
    private Integer page;

    @Schema(description = "每页大小")
    private Integer pageSize;
}
