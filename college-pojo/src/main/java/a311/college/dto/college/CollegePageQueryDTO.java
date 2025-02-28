package a311.college.dto.college;

import a311.college.enumeration.ProvinceEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class CollegePageQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 学校名
    private String schoolName;

    // 省份名
    private String province;

    // 成绩
    private Integer grade;

    // 大学类型
    private List<String> rankList;

    // 页码
    private Integer page;

    // 每页记录数
    private Integer pageSize;
}
