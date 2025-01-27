package a311.college.entity;

import a311.college.enumeration.Province;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 用户ID
    private Long id;

    private String username;
    private String password;
    private String phone;
    private String email;

    // 头像
    private String head;

    // 考试年份
    private LocalDate year;

    // 选科
    private List<String> subjects;

    // 省份
    private Province province;

    // 成绩
    private Integer grade;

    // 位次（选填）
    private Integer rank;

    // 用户状态，通过状态识别其身份 0：不可用 1：可用 7：管理员
    private Integer status;

    // 意向城市（注册之后填）
    private Province area;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
