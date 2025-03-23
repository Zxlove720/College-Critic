package a311.college.dto.school;

import a311.college.enumeration.ProvinceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 热点学校DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "热点学校DTO")
public class HotSchoolDTO {

    @Schema(description = "省份")
    private ProvinceEnum province;

}
