package a311.college.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Province implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 省份id
    private Integer id;
    // 对应的专业id
    private Integer MajorId;
    // 省份名
    private String name;
    // 分数线
    private List<ScoreLine> scoreLineList;
}
