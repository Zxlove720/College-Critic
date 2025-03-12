package a311.college.result;

import a311.college.enumeration.ProvinceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录信息返回")
public class LoginResult implements Serializable {

    @Schema(description = "登录凭证")
    private String uuid;

    @Schema(description = "用户头像")
    private String head;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户手机")
    private String phone;

    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "用户省份")
    private ProvinceEnum province;

    @Schema(description = "用户选科")
    private String subjects;

    @Schema(description = "用户成绩")
    private Integer grade;

}
