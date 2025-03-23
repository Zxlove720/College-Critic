package a311.college.database.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "招生批次")
public class Batch implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "招生批次名")
    private String batch;

    @Schema(description = "分数列表")
    private List<Score> scores;
}
