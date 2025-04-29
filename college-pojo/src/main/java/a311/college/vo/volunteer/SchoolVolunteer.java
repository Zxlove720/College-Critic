package a311.college.vo.volunteer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 学校志愿
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "学校志愿")
public class SchoolVolunteer {

    @Schema(description = "学校id")
    private Integer schoolId;

    @Schema(description = "学校校徽")
    private String schoolHead;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "所在省份")
    private String schoolProvince;

    @Schema(description = "录取概率")
    private Integer forecast;

    @Schema(description = "可报专业")
    private List<VolunteerVO> volunteerVOList;
}
