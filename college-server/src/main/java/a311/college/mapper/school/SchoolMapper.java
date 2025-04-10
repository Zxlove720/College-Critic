package a311.college.mapper.school;


import a311.college.dto.school.*;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.entity.school.School;
import a311.college.entity.school.SchoolMajor;
import a311.college.vo.school.*;
import a311.college.vo.major.MajorSimpleVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 学校相关Mapper
 */
@Mapper
public interface SchoolMapper {

    /**
     * 学校信息分页查询
     *
     * @param schoolPageQueryDTO 学校分页查询DTO
     * @return List<School> 学校实体对象
     */
    List<School> schoolPageQuery(SchoolPageQueryDTO schoolPageQueryDTO);

    /**
     * 根据省份查询学校
     *
     * @param province 省份名
     * @return List<School> 学校实体对象
     */
    @Select("select school_id, school_head, school_name, school_province, school_address, rank_list from tb_school " +
            "where school_province = #{province} order by score desc, length(rank_list) desc")
    List<School> selectSchoolByProvince(String province);

    /**
     * 专业分页查询
     *
     * @param schoolMajorPageQueryDTO 学校专业查询DTO
     * @return List<SchoolMajor> 学校专业实体对象
     */
    List<SchoolMajor> schoolMajorPageQuery(SchoolMajorPageQueryDTO schoolMajorPageQueryDTO);

    /**
     * 根据学校名搜索大学
     *
     * @param schoolName 学校名
     * @return List<School> 学校实体对象
     */
    List<School> selectSchoolBySchoolName(String schoolName);

    /**
     * 根据用户搜索内容搜索学校（搜索提示）
     *
     * @param message 搜索内容
     * @return List<School>
     */
    List<School> searchSchool(String message);

    /**
     * 查询学校具体信息
     *
     * @param schoolId 学校id
     * @return DetailedSchoolVO 学校具体信息VO
     */
    @Select("select * from tb_school where school_id = #{schoolId}")
    DetailedSchoolVO selectDetailBySchoolId(int schoolId);

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
     * 根据学校分数获取分数相近学校
     *
     * @param score 学校分数
     * @return List<School>
     */
    @Select("select school_id, school_head, school_name, rank_list from tb_school where score < #{score} order by score desc limit 5")
    List<School> selectCloseSchool(Integer score);

    /**
     * 查询某学校所有专业
     *
     * @param forecastDTO 预测DTO
     * @return List<SchoolMajorVO>
     */
    List<SchoolMajor> selectAllSchoolMajor(ForecastDTO forecastDTO);

    /**
     * 获取某学校的历年分数线
     *
     * @return List<YearScoreVO>
     */
    List<YearScoreVO> selectScoreLineByYear(YearScoreQueryDTO yearScoreDTO);

    /**
     * 添加用户评价
     *
     * @param addCommentDTO 用户评价DTO
     */
    @Insert("insert tb_comment set user_id = #{userId}, school_id = #{schoolId}, comment = #{comment}")
    void addComment(AddSchoolCommentDTO addCommentDTO);

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
     * 根据省份查询本科学校
     *
     * @param province 省份
     * @return List<SchoolVO>
     */
    @Select("select * from tb_school where school_province = #{province} and rank_list like '%本科%' order by score desc limit 9")
    List<SchoolSceneryVO> selectByProvince(String province);

    /**
     * 根据省份查询专科学校
     *
     * @param province 省份
     * @return List<SchoolVO>
     */
    @Select("select * from tb_school where school_province = #{province} and rank_list like '%专科%' order by score desc limit 9")
    List<SchoolSceneryVO> selectByProvinceProfessional(String province);

    /**
     * 查询学校评价
     *
     * @param schoolId 学校ID
     * @return CommentVO实体对象
     */
    List<CommentVO> selectComment(int schoolId);

    /**
     * 删除用户收藏
     *
     * @param schoolDTO 学校DTO
     */
    @Delete("delete from tb_fav_school where user_id = #{userId} and school_id = #{schoolId}")
    void deleteFavoriteSchool(SchoolDTO schoolDTO);

    @Select("select * from tb_school where school_province != #{province} and rank_list like '%本科%' order by score desc limit 9")
    List<SchoolSceneryVO> selectWithoutProvince(String province);

    @Select("select * from tb_school where school_province != #{province} and rank_list like '%专科%' order by score desc limit 9")
    List<SchoolSceneryVO> selectWithoutProvinceProfessional(String province);

    @Select("select school_id, school_head, school_name from tb_school where rank_list like '%强基计划%'")
    List<School> selectBasicSchool();

    @Select("select * from tb_scenery where school_name = #{schoolName}")
    SchoolSceneryVO selectUniqueSchool(String schoolName);

    @Select("select * from tb_scenery where rank_list like '%985%' and school_name != #{schoolName} limit 1")
    SchoolSceneryVO selectOtherProvinceSchool(String schoolName);

    @Select("select * from tb_scenery where rank_list like '%专科%' and school_name != #{schoolName} limit 1")
    SchoolSceneryVO selectOtherProvinceProfessional(String schoolName);
}
