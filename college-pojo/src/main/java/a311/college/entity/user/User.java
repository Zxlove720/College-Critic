package a311.college.entity.user;

import a311.college.enumeration.ProvinceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 用户类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户类")
public class User implements Serializable {

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

    @Schema(description = "用户意向城市")
    private ProvinceEnum city;

    @Schema(description = "收藏表")
    private String favoriteTable;

    @Schema(description = "志愿表")
    private String collegeTable;

    @Schema(description = "用户状态")
    private Integer status;   // 1 可用（默认） 0 不可用

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
}
