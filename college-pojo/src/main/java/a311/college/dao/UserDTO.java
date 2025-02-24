package a311.college.dao;

import a311.college.enumeration.Province;
import a311.college.enumeration.Subjects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;
    private String password;
    private String phone;
    private String email;

    // 头像
    private String head;

    // 考试年份
    private Year year;

    // 选科
    private List<Subjects> subjects;

    // 省份
    private Province province;

    // 成绩
    private Integer grade;

    // 位次（选填）
    private Integer ranking;

    // 意向城市（注册之后填）
    private Province city;
}
