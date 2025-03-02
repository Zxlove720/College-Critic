package a311.college.dto.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Major查询DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorDTO implements Serializable {

    @Schema(description = "所属层次id")
    private Integer levelId;

    @Schema(description = "所属分类id")
    private Integer categoryId;

    @Schema(description = "所属专业类id")
    private Integer professionalId;
}
