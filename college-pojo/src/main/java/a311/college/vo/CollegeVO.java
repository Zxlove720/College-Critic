package a311.college.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "大学信息VO")
public class CollegeVO implements Serializable {

    @Schema(description = "大学校徽")
    private String schoolHead;

    @Schema(description = "大学名称")
    private String schoolName;

    @Schema(description = "省份")
    private String schoolProvince;

    @Schema(description = "学校地址")
    private String schoolAddress;

    @Schema(description = "大学等级标签列表")
    private String rankList;

    @Schema(description = "校园风光")
    private List<String> images;

    @Schema(description = "开设专业")
    private List<MajorSimpleVO> majors;

    @Schema(description = "校园配置")
    private Map<String, Integer> equipment;

}