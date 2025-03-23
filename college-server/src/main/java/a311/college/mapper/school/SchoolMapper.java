package a311.college.mapper.school;


import a311.college.dto.school.AddSchoolCommentDTO;
import a311.college.dto.school.ForecastDTO;
import a311.college.dto.school.SchoolPageQueryDTO;
import a311.college.dto.query.school.UserGradeQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.entity.school.School;
import a311.college.entity.school.SchoolMajor;
import a311.college.enumeration.ProvinceEnum;
import a311.college.vo.school.SchoolMajorVO;
import a311.college.vo.major.MajorSimpleVO;
import a311.college.vo.school.SchoolVO;
import a311.college.vo.school.YearScoreVO;
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
public interface SchoolMapper {

    /**
     * 大学信息分页查询
     *
     * @param schoolPageQueryDTO 大学分页查询DTO
     * @return Page<DetailedSchoolVO>
     */
    Page<SchoolVO> pageQuery(SchoolPageQueryDTO schoolPageQueryDTO);

    /**
     * 根据省份查询大学
     *
     * @param province 省份
     * @return List<DetailedSchoolVO>
     */
    @Select("select school_head, school_name,school_address, rank_list from tb_school " +
            "where school_province = #{province} order by score desc, length(rank_list) desc limit 9")
    List<SchoolVO> selectByAddress(String province);

    /**
     * 根据学校名搜索大学
     *
     * @param schoolName 学校名
     * @return List<SchoolVO>
     */
    List<SchoolVO> selectByName(String schoolName);

    /**
     * 根据成绩查询大学
     *
     * @return List<DetailedSchoolVO>
     */
    List<SchoolVO> selectByGrade(UserGradeQueryDTO gradeDTO);

    /**
     * 获取某一院校的历年分数线
     *
     * @return Result<YearScoreVO>
     */
    List<YearScoreVO> selectScoreByYear(YearScoreQueryDTO yearScoreDTO);

    /**
     * 根据大学id查询学校
     *
     * @param schoolId 大学id
     * @return SchoolVO
     */
    @Select("select * from tb_school where school_id = #{schoolId}")
    SchoolVO selectBySchoolId(String schoolId);

    /**
     * 查询该学校开设专业
     *
     * @param schoolId 学校id
     * @return List<MajorSimpleVO>
     */
    List<MajorSimpleVO> selectSimpleMajor(int schoolId);

    /**
     * 添加用户评价
     *
     * @param addCommentDTO 用户评价DTO
     */
    @Insert("insert tb_comment set user_id = #{userId}, school_id = #{schoolId}, comment = #{comment}")
    void addComment(AddSchoolCommentDTO addCommentDTO);

    /**
     * 获取全部学校
     *
     * @return List<SchoolVO>
     */
    @Select("select * from tb_school")
    List<SchoolVO> getAllSchool();

    /**
     * 更新学校等级
     *
     * @param schoolVO 学校简略信息VO
     */
    @Update("update tb_school set rank_list = #{rankList} where school_id = #{schoolId}")
    void updateRank(SchoolVO schoolVO);

    /**
     * 更新学校客观评分
     *
     * @param schoolVO 学校简略信息VO
     */
    @Update("update tb_school set score = #{score} where school_id = #{schoolId}")
    void updateScore(SchoolVO schoolVO);

    /**
     * 查询某学校所有专业
     *
     * @param forecastDTO 预测DTO
     * @return List<SchoolMajorVO>
     */
    List<SchoolMajorVO> getAllMajor(ForecastDTO forecastDTO);

    @Select("select * from tb_score")
    List<SchoolMajor> selectAllMajor();

    @Select("select * from tb_school where school_province = #{province} order by score desc limit 9")
    List<School> selectByProvince(ProvinceEnum province);

    @Select("select * from tb_school where school_name = #{school}")
    School getByName(String school);
}
