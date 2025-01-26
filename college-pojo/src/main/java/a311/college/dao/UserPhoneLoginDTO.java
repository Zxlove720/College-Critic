package a311.college.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录（手机号）DTO
 */
@Data
@ApiModel(description = "用户登录（手机号）时传递的数据模型")
public class UserPhoneLoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("验证码")
    private String captcha;

}
