package a311.college.vo.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 专业简单信息VO
 */
@Data
public class MajorSimpleVO {

    @Schema(description = "专业所属类别")
    private String categoryName;

    @Schema(description = "专业名")
    private String MajorName;
}
