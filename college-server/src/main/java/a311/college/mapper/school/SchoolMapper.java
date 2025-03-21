package a311.college.mapper.school;


import a311.college.dto.school.AddSchoolCommentDTO;
import a311.college.dto.school.ForecastDTO;
import a311.college.dto.school.SchoolPageQueryDTO;
import a311.college.dto.query.school.UserGradeQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.entity.school.SchoolInfo;
import a311.college.entity.school.SchoolMajor;
import a311.college.enumeration.ProvinceEnum;
import a311.college.vo.school.SchoolMajorVO;
import a311.college.vo.major.MajorSimpleVO;
import a311.college.vo.school.SchoolSimpleVO;
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
     * @return Page<SchoolVO>
     */
    Page<SchoolSimpleVO> pageQuery(SchoolPageQueryDTO schoolPageQueryDTO);

    /**
     * 根据省份查询大学
     *
     * @param province 省份
     * @return List<SchoolVO>
     */
    @Select("select school_head, school_name,school_address, rank_list from tb_school " +
            "where school_province = #{province} order by score desc, length(rank_list) desc limit 9")
    List<SchoolSimpleVO> selectByAddress(String province);

    /**
     * 根据学校名搜索大学
     *
     * @param schoolName 学校名
     * @return List<SchoolSimpleVO>
     */
    List<SchoolSimpleVO> selectByName(String schoolName);

    /**
     * 根据成绩查询大学
     *
     * @return List<SchoolVO>
     */
    List<SchoolSimpleVO> selectByGrade(UserGradeQueryDTO gradeDTO);

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
     * @return SchoolSimpleVO
     */
    @Select("select * from tb_school where school_id = #{schoolId}")
    SchoolSimpleVO selectBySchoolId(String schoolId);

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
     * @return List<SchoolSimpleVO>
     */
    @Select("select * from tb_school")
    List<SchoolSimpleVO> getAllSchool();

    /**
     * 更新学校等级
     *
     * @param schoolSimpleVO 学校简略信息VO
     */
    @Update("update tb_school set rank_list = #{rankList} where school_id = #{schoolId}")
    void updateRank(SchoolSimpleVO schoolSimpleVO);

    /**
     * 更新学校客观评分
     *
     * @param schoolSimpleVO 学校简略信息VO
     */
    @Update("update tb_school set score = #{score} where school_id = #{schoolId}")
    void updateScore(SchoolSimpleVO schoolSimpleVO);

    /**
     * 查询某学校所有专业
     *
     * @param forecastDTO 预测DTO
     * @return List<SchoolMajorVO>
     */
    List<SchoolMajorVO> getAllMajor(ForecastDTO forecastDTO);

    @Select("select * from tb_score")
    List<SchoolMajor> selectAllMajor();

    @Update("update tb_score set major_name = #{majorName}, first_choice = #{firstChoice}, other_choice = #{otherChoice}, " +
            "special = #{special}")
    void updateMajor(SchoolMajor schoolMajor);

    @Select("select * from tb_school where school_province = #{province} order by score desc limit 9")
    List<SchoolInfo> selectByProvince(ProvinceEnum province);

    @Select("select * from tb_school where school_name = #{school}")
    SchoolInfo getByName(String school);
}
