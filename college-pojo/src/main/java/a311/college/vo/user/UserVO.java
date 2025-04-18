package a311.college.vo.user;

import a311.college.enumeration.ProvinceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

/**
 * 用户个人主页VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户手机号")
    private String phone;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String head;

    @Schema(description = "用户毕业年份")
    private Year year;

    @Schema(description = "用户所在省份")
    private ProvinceEnum province;

    @Schema(description = "首选科目")
    private String firstChoice;

    @Schema(description = "用户选科（新高考）")
    private String subjects;

    @Schema(description = "用户成绩")
    private Integer grade;

    @Schema(description = "用户位次")
    private Integer ranking;

    @Schema(description = "用户意向城市")
    private ProvinceEnum city;
}
