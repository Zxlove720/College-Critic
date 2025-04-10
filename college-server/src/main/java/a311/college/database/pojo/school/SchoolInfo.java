package a311.college.database.pojo.school;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolInfo implements Serializable {
    @JsonProperty("SchoolName")
    private String schoolName;

    @JsonProperty("RankingInfo")
    private List<List<String>> rankingInfo;

    @JsonProperty("PopularityValue")
    private String popularityValue;

    @JsonProperty("OfficialWebsite")
    private String officialWebsite;

    @JsonProperty("SchoolPhoneNumber")
    private String schoolPhoneNumber;

    @JsonProperty("SchoolMailbox")
    private String schoolMailbox;

    @JsonProperty("scholarship")
    private String scholarship;
}
