package a311.college.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 招生批次
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Batch implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "招生批次名")
    private String batch;

    @Schema(description = "分数列表")
    private List<Score> scores;
}
