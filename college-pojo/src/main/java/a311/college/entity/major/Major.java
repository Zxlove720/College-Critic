package a311.college.entity.major;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 专业详情
 */
@Data
public class Major implements Serializable {
    @JsonProperty("专业名称")
    private String majorName;
    @JsonProperty("专业代码")
    private String majorCode;
    @JsonProperty("修业年限")
    private String majorYear;
    @JsonProperty("授予学位")
    private List<String> majorDegree;
    @JsonProperty("男女比例")
    private String gender;
    @JsonProperty("平均薪酬")
    private Integer averageSalary;
}
