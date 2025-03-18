package a311.college.entity.college;

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
@Schema(description = "招生年份")
public class Years implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "招生年份")
    private String year;

    @Schema(description = "招生分类")
    private List<Category> categories;

}
