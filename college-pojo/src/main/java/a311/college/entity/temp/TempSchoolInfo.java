package a311.college.entity.temp;

import a311.college.entity.school.Province;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempSchoolInfo implements Serializable {
    @Schema(description = "大学名")
    @JsonProperty("SchoolName")
    private String schoolName;

    @Schema(description = "招生省份")
    @JsonProperty("Provinces")
    private List<Province> provinces;
}
