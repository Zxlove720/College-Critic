package a311.college.mapper.college;


import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.vo.CollegeVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 大学相关Mapper
 */
@Mapper
public interface CollegeMapper {

    /**
     * 大学信息分页查询
     * @param collegePageQueryDTO 大学分页查询DTO
     * @return Page<CollegeVO>
     */
    Page<CollegeVO> pageQuery(CollegePageQueryDTO collegePageQueryDTO);

    /**
     * 根据学校名查询大学
     * @param schoolName 学校名
     * @return CollegeVO
     */
    @Select("select * from tb_school where school_name = #{schoolName}")
    CollegeVO selectByName(String schoolName);

    /**
     * 根据成绩查询大学
     * @param grade 用户成绩
     * @return List<CollegeVO>
     */
    List<CollegeVO> selectByGrade(int grade, String province);

    /**
     * 根据省份查询大学
     * @param province 省份
     * @return List<CollegeVO>
     */
    List<CollegeVO> selectByAddress(String province);
}
