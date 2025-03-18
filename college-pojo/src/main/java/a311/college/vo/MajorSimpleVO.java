package a311.college.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MajorSimpleVO {

    @Schema(description = "专业所属类别")
    private String categoryName;

    @Schema(description = "专业名")
    private String MajorName;
}
