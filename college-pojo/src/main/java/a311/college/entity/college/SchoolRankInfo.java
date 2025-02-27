package a311.college.entity.college;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 学校的扩展数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolRankInfo {
    @JsonProperty("SchoolName")
    private String schoolName;

    @JsonProperty("SchoolAddr")
    private String schoolAddr;

    @JsonProperty("RankList")
    private List<String> rankList;

    @JsonProperty("Batch")
    private String batch;
}
