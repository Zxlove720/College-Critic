package a311.college.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 大学类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class College implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 学校ID
    private Integer id;
    // 学校名
    private String name;
    // 学校地理位置
    private String province;
    // 学校等级 1：985 2：211 3：一本 4：二本 5：民办 6：专科
    private Integer rank;
    // 学校的专业，包含了该专业的历年分数线、招生计划、招生批次
    private List<Major> majors;
    // 如果等级是985 或 211 那么标记为重点
    private Boolean important;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
