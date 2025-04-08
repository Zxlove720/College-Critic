package a311.college.vo.school;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 热门院校VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "热门院校VO")
public class HotSchoolVO {

    @Schema(description = "学校id")
    private Integer schoolId;

    @Schema(description = "学校校徽")
    private String schoolHead;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "学校热度")
    private Integer hot;
}
