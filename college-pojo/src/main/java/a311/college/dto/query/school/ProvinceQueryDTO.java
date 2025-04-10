package a311.college.dto.query.school;

import a311.college.enumeration.ProvinceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 省份查询DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "省份查询DTO")
public class ProvinceQueryDTO {

    @Schema(description = "所在省份")
    private ProvinceEnum province;

}
