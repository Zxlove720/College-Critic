package a311.college.database.temp;

import a311.college.enumeration.ProvinceEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "大学扩展信息（保存数据用）")
public class TempSchoolRankInfo {
    @JsonProperty("SchoolName")
    private String schoolName;

    @JsonProperty("SchoolAddr")
    private String schoolAddr;

    @JsonProperty("RankList")
    private List<String> rankList;

    @JsonProperty("Batch")
    private String batch;
}
