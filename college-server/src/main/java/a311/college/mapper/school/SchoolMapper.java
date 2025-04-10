package a311.college.mapper.school;


import a311.college.dto.school.*;
import a311.college.dto.query.school.GradePageQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.entity.school.School;
import a311.college.entity.school.SchoolMajor;
import a311.college.vo.school.*;
import a311.college.vo.major.MajorSimpleVO;
import org.apache.ibatis.annotations.*;

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
    List<School> pageQuery(SchoolPageQueryDTO schoolPageQueryDTO);

    /**
     * 根据省份查询大学
     *
     * @param province 省份
     * @return List<DetailedSchoolVO>
     */
    @Select("select school_id, school_head, school_name, school_province, school_address, rank_list from tb_school " +
            "where school_province = #{province} order by score desc, length(rank_list) desc")
    List<School> selectByAddress(String province);

    /**
     * 根据学校名搜索大学
     *
     * @param schoolName 学校名
     * @return List<SchoolVO>
     */
    List<School> searchBySchoolName(String schoolName);

    /**
     * 根据成绩查询大学
     *
     * @return List<DetailedSchoolVO>
     */
    List<School> selectByGrade(GradePageQueryDTO gradeDTO);

    /**
     * 获取某一院校的历年分数线
     *
     * @return Result<YearScoreVO>
     */
    List<YearScoreVO> selectScoreByYear(YearScoreQueryDTO yearScoreDTO);

    /**
     * 根据大学id查询大学
     *
     * @param schoolId 大学id
     * @return SchoolVO
     */
    @Select("select * from tb_school where school_id = #{schoolId}")
    School selectBySchoolId(int schoolId);

    /**
     * 查询该学校开设专业
     *
     * @param schoolId 学校id
     * @return List<MajorSimpleVO>
     */
    List<MajorSimpleVO> selectMajor(int schoolId);

    /**
     * 添加用户评价
     *
     * @param addCommentDTO 用户评价DTO
     */
    @Insert("insert tb_comment set user_id = #{userId}, school_id = #{schoolId}, comment = #{comment}")
    void addComment(AddSchoolCommentDTO addCommentDTO);

    /**
     * 查询某学校所有专业
     *
     * @param forecastDTO 预测DTO
     * @return List<SchoolMajorVO>
     */
    List<SchoolMajor> selectAllMajor(ForecastDTO forecastDTO);

    /**
     * 根据省份查询本科学校
     *
     * @param province 省份
     * @return List<SchoolVO>
     */
    @Select("select * from tb_school where school_province = #{province} and rank_list like '%本科%' order by score desc limit 9")
    List<School> selectByProvince(String province);

    /**
     * 根据省份查询专科学校
     *
     * @param province 省份
     * @return List<SchoolVO>
     */
    @Select("select * from tb_school where school_province = #{province} and rank_list like '%专科%' order by score desc limit 9")
    List<School> selectByProvinceProfessional(String province);

    /**
     * 根据学校名查询学校
     *
     * @param schoolName 学校名
     * @return School 学校实体对象
     */
    @Select("select * from tb_school where school_name = #{schoolName}")
    School selectBySchoolName(String schoolName);

    /**
     * 专业分页查询
     *
     * @param schoolMajorPageQueryDTO 学校专业查询DTO
     * @return SchoolMajor 学校专业实体对象
     */
    List<SchoolMajor> pageQuerySchoolMajor(SchoolMajorPageQueryDTO schoolMajorPageQueryDTO);

    /**
     * 查询学校评价
     *
     * @param schoolId 学校ID
     * @return CommentVO实体对象
     */
    List<CommentVO> selectComment(int schoolId);

    /**
     * 学校搜索
     *
     * @param message 搜索信息
     * @return BriefSchoolInfoVO 简略学校信息
     */
    List<School> searchSchool(String message);

    /**
     * 判断该学校是否已经被收藏
     *
     * @param schoolDTO 大学查询DTO
     * @return int 收藏表中符合条件学校数量
     */
    @Select("select count(school_id) from tb_fav_school where user_id = #{userId} and school_id = #{schoolId}")
    int checkSchoolDistinct(SchoolDTO schoolDTO);

    /**
     * 添加用户收藏
     *
     * @param schoolDTO 学校DTO
     */
    @Select("insert into tb_fav_school (user_id, school_id) VALUES (#{userId}, #{schoolId})")
    void addFavoriteSchool(SchoolDTO schoolDTO);

    /**
     * 删除用户收藏
     *
     * @param schoolDTO 学校DTO
     */
    @Delete("delete from tb_fav_school where user_id = #{userId} and school_id = #{schoolId}")
    void deleteFavoriteSchool(SchoolDTO schoolDTO);

    @Select("select * from tb_school where school_province != #{province} and rank_list like '%本科%' order by score desc limit 9")
    List<School> selectWithoutProvince(String province);

    @Select("select * from tb_school where school_province != #{province} and rank_list like '%专科%' order by score desc limit 9")
    List<School> selectWithoutProvinceProfessional(String province);

    @Select("select school_id, school_head, school_name from tb_school where rank_list like '%强基计划%'")
    List<School> selectBasicSchool();

    @Select("select school_id, school_head, school_name, rank_list from tb_school where score < #{score} order by score desc limit 5")
    List<School> selectCloseSchool(Integer score);

}
