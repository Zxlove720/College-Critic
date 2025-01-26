package a311.college.dao;


import java.io.Serial;
import java.io.Serializable;

public class PasswordEditPhoneDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 员工id
    private Long userId;

    // 手机号
    private String phone;

    // 验证码
    private String captcha;

    // 密码
    private String password;

}
