package a311.college.vo.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 热门专业展示VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "热门专业展示VO")
public class HotMajorVO {

    @Schema(description = "专业id")
    private Integer majorId;

    @Schema(description = "专业名")
    private String majorName;

    @Schema(description = "专业信息")
    private String majorClass;

    @Schema(description = "专业对应学校名")
    private String schoolName;

    @Schema(description = "学校id")
    private Integer schoolId;

    @Schema(description = "最低分数")
    private Integer minScore;


}
