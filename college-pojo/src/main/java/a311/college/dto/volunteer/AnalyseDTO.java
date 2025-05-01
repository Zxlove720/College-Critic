package a311.college.dto.volunteer;

import io.swagger.v3.oas.annotations.media.Schema;

public class AnalyseDTO {

    @Schema(description = "志愿表id")
    private Integer tableId;

    @Schema(description = "用户成绩")
    private Integer grade;

    @Schema(description = "用户位次")
    private Integer ranking;

}
