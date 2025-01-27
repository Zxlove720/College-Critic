package a311.college.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 分数线
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreLine implements Serializable {

    // 分数线id
    private String id;
    // 该分数线对应的专业id
    private Integer majorId;

    // 年份
    private Integer year;
    // 最低分
    private Integer minScore;
    // 最低位次
    private Integer rank;
}
