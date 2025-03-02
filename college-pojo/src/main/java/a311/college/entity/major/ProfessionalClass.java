package a311.college.entity.major;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 专业
 */
@Data
public class ProfessionalClass implements Serializable {
    @JsonProperty("专业")
    private List<Major> majorList;

}
