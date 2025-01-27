package a311.college.entity;

import a311.college.enumeration.Province;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 专业
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Major implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 专业id
    private Integer id;
    // 所属大学id
    private Integer collegeId;
    // 专业名称
    private String name;
    // 录取批次，提前批、本科、专科批等
    private Integer admissionBatch;
    // 招生省份
    private List<Province> provinceList;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
