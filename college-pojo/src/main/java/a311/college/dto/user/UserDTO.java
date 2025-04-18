package a311.college.dto.user;

import a311.college.enumeration.ProvinceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户DTO
 */
@Data
@Schema(description = "用户DTO")
public class UserDTO {

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "用户手机号")
    private String phone;

    @Schema(description = "用户头像")
    private String head;

    @Schema(description = "用户毕业年份")
    private Integer year;

    @Schema(description = "用户所在省份")
    private ProvinceEnum province;

    @Schema(description = "用户选科（新高考）")
    private String subjects;

    @Schema(description = "用户成绩")
    private Integer grade;

    @Schema(description = "用户位次")
    private Integer ranking;


}
