package a311.college.database.pojo.major;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;


@Data
@Schema(description = "培养层次，保存数据用")
public class AcademicLevel implements Serializable {

    @JsonProperty("学科门类")
    private Map<String, SubjectCategory> subjectCategoryMap;
}
