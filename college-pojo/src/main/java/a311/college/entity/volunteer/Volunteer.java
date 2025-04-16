package a311.college.entity.volunteer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 志愿实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "志愿实体类")
public class Volunteer {

    @Schema(description = "志愿id")
    private Integer volunteerId;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "所属类型")
    private Integer category;

    @Schema(description = "学校id")
    private String schoolId;

    @Schema(description = "学校校徽")
    private String schoolHead;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "所在省份")
    private String schoolProvince;

    @Schema(description = "等级列表")
    private String rankList;

    @Schema(description = "专业id")
    private Integer majorId;

    @Schema(description = "首选科目")
    private String firstChoice;

    @Schema(description = "次选科目")
    private String otherChoice;

    @Schema(description = "特殊要求")
    private String special;

    @Schema(description = "志愿数")
    private Integer count;

    @Schema(description = "招生年份")
    private Integer year;

    @Schema(description = "最低分数")
    private Integer minScore;

    @Schema(description = "最低位次 ")
    private Integer minRanking;

    @Schema(description = "最低分与用户相比")
    private Integer scoreThanMe;

    @Schema(description = "最低位次与用户相比")
    private Integer rankingThanMe;
}
