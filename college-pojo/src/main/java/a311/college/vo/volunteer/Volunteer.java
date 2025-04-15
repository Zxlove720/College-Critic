package a311.college.vo.volunteer;

import io.swagger.v3.oas.annotations.media.Schema;

public class Volunteer {

    @Schema(description = "所属类型")
    private Integer category;

    @Schema(description = "学校名称")
    private Integer schoolId;


}
