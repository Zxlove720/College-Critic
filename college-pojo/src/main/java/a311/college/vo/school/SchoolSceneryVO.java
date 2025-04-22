package a311.college.vo.school;

import a311.college.entity.school.SchoolMajor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 首页校园风光展示VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "首页校园风光展示VO")
public class SchoolSceneryVO {

    @Schema(description = "学校id")
    private Integer schoolId;

    @Schema(description = "学校校徽")
    private String schoolHead;

    @Schema(description = "学校名")
    private String schoolName;

    @Schema(description = "所在省份")
    private String province;

    @Schema(description = "等级列表")
    private String rankList;

    @Schema(description = "校园风光")
    private String image;

    @Schema(description = "排名项目")
    private String rankItem;

    @Schema(description = "排名信息")
    private String rankInfo;

    @Schema(description = "大学具体排名")
    private Map<String, String> rank;

    @Schema(description = "开设专业")
    private List<SchoolMajor> majors;

}
