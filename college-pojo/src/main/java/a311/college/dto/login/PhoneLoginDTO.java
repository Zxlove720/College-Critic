package a311.college.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "用户手机登录DTO")
public class PhoneLoginDTO implements Serializable {

    @Schema(description = "手机号")
    public String phone;

    @Schema(description = "验证码")
    public String code;
}
