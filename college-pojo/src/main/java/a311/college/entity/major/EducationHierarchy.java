package a311.college.entity.major;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;


@Data
@Schema(description = "专业，保存数据用")
public class EducationHierarchy implements Serializable {
    @JsonProperty("学历层次")
    private Map<String, AcademicLevel> academicLevelMap;
}
