package a311.college.entity.college;

import a311.college.enumeration.ProvinceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 招生省份类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Province implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "省份名")
    private ProvinceEnum province;

    @Schema(description = "历年分数线")
    private List<Years> years;

}
