package a311.college.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录（手机号）DTO
 */
@Data
public class UserPhoneLoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String phone;

    private String code;

}
