package a311.college.entity;

import a311.college.enumeration.ProvinceEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("SchoolName")
    private String schoolName;

    @Schema(description = "招生省份")
    @JsonProperty("Provinces")
    private List<Province> provinces;

}
