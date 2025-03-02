package a311.college.entity.major;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 学历层次
 */
@Data
public class AcademicLevel implements Serializable {
    @JsonProperty("学科门类")
    private Map<String, SubjectCategory> subjectCategoryMap;
}
