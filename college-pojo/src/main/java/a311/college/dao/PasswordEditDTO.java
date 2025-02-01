package a311.college.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户修改密码（原始）DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
