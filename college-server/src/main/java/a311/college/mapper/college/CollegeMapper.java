package a311.college.mapper.college;


import a311.college.dto.college.AddCommentDTO;
import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.dto.query.school.GradeDTO;
import a311.college.dto.query.school.YearScoreDTO;
import a311.college.vo.CollegeSimpleVO;
import a311.college.vo.YearScoreVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
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
     *
     * @param collegePageQueryDTO 大学分页查询DTO
     * @return Page<CollegeVO>
     */
    Page<CollegeSimpleVO> pageQuery(CollegePageQueryDTO collegePageQueryDTO);

    /**
     * 根据成绩查询大学
     *
     * @return List<CollegeVO>
     */
    List<CollegeSimpleVO> selectByGrade(GradeDTO gradeDTO);

    /**
     * 根据省份查询大学
     *
     * @param province 省份
     * @return List<CollegeVO>
     */
    @Select("select school_head, school_name,school_address, rank_list from tb_school " +
            "where school_province = #{province} order by length(rank_list) desc")
    List<CollegeSimpleVO> selectByAddress(String province);

    /**
     * 获取某一院校的历年分数线
     *
     * @return Result<YearScoreVO>
     */
    List<YearScoreVO> selectScoreByYear(YearScoreDTO yearScoreDTO);

    /**
     * 根据学校名搜索
     *
     * @param schoolName 学校名
     * @return Result<List < CollegeSimpleVO>>
     */
    List<CollegeSimpleVO> selectByName(String schoolName);

    @Select("select * from tb_school where school_id = #{schoolId}")
    CollegeSimpleVO selectBySchoolId(String schoolId);

    @Insert("insert tb_comment set user_id = #{userId}, school_id = #{schoolId}, comment = #{comment}")
    void addComment(AddCommentDTO addCommentDTO);
}
