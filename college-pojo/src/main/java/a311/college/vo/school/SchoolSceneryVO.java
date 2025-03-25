package a311.college.vo.school;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 首页校园风光展示VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "首页校园风光展示VO")
public class SchoolSceneryVO {

    @Schema(description = "校园风光")
    private String image;

    @Schema(description = "校徽")
    private String schoolHead;

    @Schema(description = "学校名")
    private String schoolName;
}
