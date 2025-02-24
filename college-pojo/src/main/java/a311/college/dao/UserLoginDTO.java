package a311.college.dao;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录（账号密码）DTO
 */
@Data
public class UserLoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

}
