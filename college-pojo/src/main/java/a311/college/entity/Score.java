package a311.college.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分数信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "专业名")
    private String majorName;

    @Schema(description = "选科要求")
    private String subjectRequirement;

    @Schema(description = "低分及位次")
    private String minScore;

    @Schema(description = "最低分")
    private int scoreValue;

    @Schema(description = "最低位次")
    private int ranking;

}
