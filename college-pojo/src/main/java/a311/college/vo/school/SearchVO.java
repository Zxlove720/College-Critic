package a311.college.vo.school;

import a311.college.entity.major.Major;
import a311.college.entity.school.School;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 搜索返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "搜索返回结果")
public class SearchVO {

    @Schema(description = "学校简略信息")
    private List<School> schoolVOList;

    @Schema(description = "专业简略信息")
    private List<Major> majorList;
}
