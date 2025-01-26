package a311.college.dao;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户修改密码（原始）DTO
 */
public class PasswordEditDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 用户id
    private Long userId;

    // 旧密码
    private String oldPassword;

    // 新密码
    private String newPassword;

}
