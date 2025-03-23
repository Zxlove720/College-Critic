package a311.college.database.temp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempSchoolID implements Serializable {

    @JsonProperty("school_id")
    private String schoolId;

    private String name;

}
