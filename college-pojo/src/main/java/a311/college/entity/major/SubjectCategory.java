package a311.college.entity.major;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;


@Data
@Schema(description = "学科门类")
public class SubjectCategory implements Serializable {
    @JsonProperty("专业类")
    private Map<String, ProfessionalClass> professionalClassMap;
}
