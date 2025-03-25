package a311.college.vo.school;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "大学信息简略展示VO")
public class BriefSchoolInfoVO {

    @Schema(description = "学校校徽")
    private String schoolHead;

    @Schema(description = "学校名")
    private String schoolName;

    @Schema(description = "学校等级")
    private String rankList;

    @Schema(description = "学校地址")
    private String schoolAddress;

}
