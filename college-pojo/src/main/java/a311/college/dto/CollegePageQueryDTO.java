package a311.college.dto;

import a311.college.enumeration.ProvinceEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CollegePageQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 学校名
    private String schoolName;

    // 省份
    private ProvinceEnum province;

    // 年份
    private Integer year;

    // 批次
    private String batch;

    // 页码
    private Integer page;

    // 每页记录数
    private Integer pageSize;
}
