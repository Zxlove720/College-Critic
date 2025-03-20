package a311.college.vo.school;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 录取概率VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "录取概率VO")
public class ForecastVO {

    @Schema(description = "可选专业个数")
    private Integer selectableMajor;

    @Schema(description = "录取概率")
    private Double chance;

    @Schema(description = "专业列表")
    private List<SchoolMajorVO> majorForecastResultList;

}
