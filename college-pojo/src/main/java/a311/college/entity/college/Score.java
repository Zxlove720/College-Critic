package a311.college.entity.college;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Score implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "本科批")
    private String batch;

    @Schema(description = "专业名")
    private String major;

    @Schema(description = "最低位次")
    private String minScore_weici;

}
