package a311.college.entity.school;

import a311.college.enumeration.ProvinceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 大学实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "大学实体类")
public class SchoolInfo implements Serializable {

    @Schema(description = "院校id")
    private Integer schoolId;

    @Schema(description = "校徽")
    private String schoolHead;

    @Schema(description = "学校名")
    private String schoolName;

    @Schema(description = "学校省份")
    private ProvinceEnum schoolProvince;

    @Schema(description = "学校具体地址")
    private String schoolAddress;

    @Schema(description = "学校等级列表")
    private String rankList;

    @Schema(description = "学校客观得分")
    private String score;
}
