package a311.college.vo.volunteer;

import io.swagger.v3.oas.annotations.media.Schema;

public class ScoreLine {

    @Schema(description = "招生年份")
    private Integer year;

    @Schema(description = "最低分数")
    private Integer minScore;

    @Schema(description = "最低位次")
    private Integer minRanking;

    @Schema(description = "最低分和用户分相比")
    private Integer scoreThanMe;

    @Schema(description = "最低位次和用户位次相比")
    private Integer rankingThanMe;


}
