package a311.college.mapper.college;


import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.result.Result;
import a311.college.vo.CollegeSimpleVO;
import a311.college.vo.YearScoreVO;
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
    Page<CollegeSimpleVO> pageQuery(CollegePageQueryDTO collegePageQueryDTO);

    /**
     * 根据成绩查询大学
     * @param grade 用户成绩
     * @return List<CollegeVO>
     */
    List<CollegeSimpleVO> selectByGrade(int grade, String province);

    /**
     * 根据省份查询大学
     * @param province 省份
     * @return List<CollegeVO>
     */
    @Select("select school_head, school_name,school_address, rank_list from tb_school " +
            "where school_province = #{province} order by length(rank_list) desc")
    List<CollegeSimpleVO> selectByAddress(String province);

    /**
     * 获取某一院校的历年分数线
     * @param id 学校id
     * @param province 招生省份
     * @param year 招生年份
     * @return Result<YearScoreVO>
     */
    List<YearScoreVO> selectScoreByYear(int id, String province, String year);

    /**
     * 根据学校名搜索
     * @param schoolName 学校名
     * @return Result<List<CollegeSimpleVO>>
     */
    List<CollegeSimpleVO> selectByName(String schoolName);
}
