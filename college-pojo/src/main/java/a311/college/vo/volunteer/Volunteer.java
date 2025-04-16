package a311.college.vo.volunteer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 志愿类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "志愿类型")
public class Volunteer {

    @Schema(description = "所属类型")
    private Integer category;

    @Schema(description = "专业id")
    private Integer majorId;

    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "首选科目")
    private String firstChoice;

    @Schema(description = "次选科目")
    private String otherChoice;

    @Schema(description = "特殊要求")
    private String special;

    @Schema(description = "是否被收藏")
    private Boolean isAdd = false;

    @Schema(description = "志愿数")
    private Integer count;

    @Schema(description = "历年分数线")
    private List<ScoreLine> scoreLineList;

}
