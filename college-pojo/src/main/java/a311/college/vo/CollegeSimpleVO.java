package a311.college.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 大学简单数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollegeSimpleVO implements Serializable {

    @Schema(description = "大学id")
    private Integer schoolId;

    @Schema(description = "大学校徽")
    private String schoolHead;

    @Schema(description = "大学名称")
    private String schoolName;

    @Schema(description = "学校地址")
    private String schoolAddress;

    @Schema(description = "大学等级标签列表")
    private String rankList;
}
