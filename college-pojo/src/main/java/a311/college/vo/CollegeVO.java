package a311.college.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 大学数据响应VO类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollegeVO implements Serializable {

    @Schema(description = "大学校徽")
    private String schoolHead;

    @Schema(description = "大学名称")
    private String schoolName;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "学校地址")
    private String schoolAddr;

    @Schema(description = "大学等级标签列表")
    private String rankList;

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "招生类别名称")
    private String category;

    @Schema(description = "批次")
    private String batch;

    @Schema(description = "专业")
    private String major;

    @Schema(description = "最低分数")
    private String minScore;

    @Schema(description = "最低位次")
    private String minRanking;

}