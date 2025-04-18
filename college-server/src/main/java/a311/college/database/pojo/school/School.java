package a311.college.database.pojo.school;

import a311.college.enumeration.ProvinceEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "大学类")
public class School implements Serializable {

    @Schema(description = "大学id")
    private String schoolId;

    @Schema(description = "大学校徽")
    private String schoolHead;

    @Schema(description = "大学名")
    @JsonProperty("SchoolName")
    private String schoolName;

    @Schema(description = "学校省份")
    private ProvinceEnum provinceAddress;

    @Schema(description = "具体地址")
    @JsonProperty("SchoolAddr")
    private String schoolAddr;

    @Schema(description = "大学等级标签列表")
    @JsonProperty("RankList")
    private String rankList;

    @Schema(description = "大学客观得分")
    private Integer score;

    @Schema(description = "招生省份")
    @JsonProperty("Provinces")
    private List<Province> provinces;

}
