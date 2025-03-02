package a311.college.entity.major;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 专业顶层对象
 */
@Data
public class EducationHierarchy implements Serializable {
    @JsonProperty("学历层次")
    private Map<String, AcademicLevel> academicLevelMap;
}
