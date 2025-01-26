package a311.college.dao;

import java.io.Serial;
import java.io.Serializable;

public class CollegePageQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 学校名
    private String collegeName;

    // 页码
    private Integer page;

    // 每页记录数
    private Integer pageSize;
}
