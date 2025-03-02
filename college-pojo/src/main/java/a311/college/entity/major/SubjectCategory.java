package a311.college.entity.major;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 学科门类
 */
@Data
public class SubjectCategory implements Serializable {
    @JsonProperty("专业类")
    private Map<String, ProfessionalClass> professionalClassMap;
}
