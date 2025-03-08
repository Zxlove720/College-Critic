package a311.college.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class PhoneLoginDTO implements Serializable {

    @Schema(description = "手机号")
    public String phone;

    @Schema(description = "验证码")
    public String code;
}
