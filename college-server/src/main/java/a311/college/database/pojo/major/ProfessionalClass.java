package a311.college.database.pojo.major;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@Schema(description = "专业类别")
public class ProfessionalClass implements Serializable {
    @JsonProperty("专业")
    private List<Major> majorList;

}
