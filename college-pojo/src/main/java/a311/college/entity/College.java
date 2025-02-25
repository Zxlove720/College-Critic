package a311.college.entity;

import a311.college.enumeration.ProvinceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 大学类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class College implements Serializable {

    @Schema(description = "大学名")
    private String collegeName;

    @Schema(description = "招生省份")
    private List<ProvinceEnum> provinces;

}
