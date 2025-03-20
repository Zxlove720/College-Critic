package a311.college.vo.school;

import a311.college.enumeration.ProvinceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 大学简单信息VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "大学简单信息VO")
public class SchoolSimpleVO implements Serializable {

    @Schema(description = "大学id")
    private Integer schoolId;

    @Schema(description = "大学校徽")
    private String schoolHead;

    @Schema(description = "大学名称")
    private String schoolName;

    @Schema(description = "大学省份")
    private ProvinceEnum schoolProvince;

    @Schema(description = "学校地址")
    private String schoolAddress;

    @Schema(description = "大学等级标签列表")
    private String rankList;

    @Schema(description = "大学等级得分")
    private Integer score;
}
