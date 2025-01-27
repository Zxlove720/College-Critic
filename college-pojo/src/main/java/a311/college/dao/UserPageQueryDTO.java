package a311.college.dao;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserPageQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 用户名
    private String username;

    // 页码
    private Integer page;

    // 每页记录数
    private Integer pageSize;
}
