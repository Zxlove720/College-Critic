package a311.college.dto.user;

import a311.college.enumeration.ProvinceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Year;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户DTO")
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户名")
    private String username;
    @Schema(description = "用户密码")
    private String password;
    @Schema(description = "用户手机号")
    private String phone;
    @Schema(description = "用户电子邮箱")
    private String email;

    @Schema(description = "用户昵称")
    private String nickname;
    @Schema(description = "用户头像")
    private String head;

    @Schema(description = "用户毕业年份")
    private Year year;

    @Schema(description = "用户所在省份")
    private ProvinceEnum province;

    @Schema(description = "高考模式")
    private Integer pattern;   // 1.新高考 0.老高考

    @Schema(description = "用户选科（新高考）")
    private String subjects;

    @Schema(description = "用户分科（老高考）")
    private Integer category;   // 1.理科 0.文科

    @Schema(description = "用户成绩")
    private Integer grade;

    @Schema(description = "用户位次")
    private Integer ranking;

    @Schema(description = "用户意向城市")
    private ProvinceEnum city;

    @Schema(description = "收藏表")
    private String favoriteTable;

    @Schema(description = "志愿表")
    private String collegeTable;
}
