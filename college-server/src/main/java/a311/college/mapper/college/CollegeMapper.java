package a311.college.mapper.college;


import a311.college.dto.school.AddSchoolCommentDTO;
import a311.college.dto.school.SchoolPageQueryDTO;
import a311.college.dto.query.school.UserGradeQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.vo.MajorSimpleVO;
import a311.college.vo.SchoolSimpleVO;
import a311.college.vo.YearScoreVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    Page<SchoolSimpleVO> pageQuery(SchoolPageQueryDTO collegePageQueryDTO);

    /**
     * 根据成绩查询大学
     *
     * @return List<CollegeVO>
     */
    List<SchoolSimpleVO> selectByGrade(UserGradeQueryDTO gradeDTO);

    /**
     * 根据省份查询大学
     *
     * @param province 省份
     * @return List<CollegeVO>
     */
    @Select("select school_head, school_name,school_address, rank_list from tb_school " +
            "where school_province = #{province} order by score desc, length(rank_list) desc")
    List<SchoolSimpleVO> selectByAddress(String province);

    /**
     * 获取某一院校的历年分数线
     *
     * @return Result<YearScoreVO>
     */
    List<YearScoreVO> selectScoreByYear(YearScoreQueryDTO yearScoreDTO);

    /**
     * 根据学校名搜索
     *
     * @param schoolName 学校名
     * @return Result<List < SchoolSimpleVO>>
     */
    List<SchoolSimpleVO> selectByName(String schoolName);

    @Select("select * from tb_school where school_id = #{schoolId}")
    SchoolSimpleVO selectBySchoolId(String schoolId);

    @Insert("insert tb_comment set user_id = #{userId}, school_id = #{schoolId}, comment = #{comment}")
    void addComment(AddSchoolCommentDTO addCommentDTO);

    @Select("select * from tb_school")
    List<SchoolSimpleVO> getAllCollege();

    @Update("update tb_school set rank_list = #{rankList} where school_id = #{schoolId}")
    void updateRank(SchoolSimpleVO collegeSimpleVO);

    @Update("update tb_school set score = #{score} where school_id = #{schoolId}")
    void updateScore(SchoolSimpleVO collegeSimpleVO);

    List<MajorSimpleVO> selectSimpleMajor(int schoolId);

}
