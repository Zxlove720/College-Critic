package a311.college.mapper.volunteer;

import a311.college.dto.user.VolunteerPageDTO;
import a311.college.entity.volunteer.Volunteer;
import a311.college.vo.volunteer.SchoolVolunteer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VolunteerMapper {

    /**
     * 查询志愿学校
     *
     * @param volunteerPageDTO 用户志愿分页查询DTO
     * @return List<School>
     */
    List<SchoolVolunteer> selectVolunteerSchool(VolunteerPageDTO volunteerPageDTO);

    /**
     * 根据id查询专业
     * @param majorId 专业id
     */
    Volunteer selectSchoolMajorById(Integer majorId);


    @Select("select count from tb_volunteer where table_id = #{tableId} order by count desc limit 1")
    Integer getCount(int tableId);

    /**
     * 添加志愿
     * @param volunteer 志愿实体
     */
    @Insert("insert into tb_volunteer(user_id, table_id, category, school_id, school_head, school_name, " +
            "school_province, rank_list, major_id, major_name, first_choice, other_choice, " +
            "special, count, year, min_score, min_ranking, scoreThanMe, rankingThanMe) values " +
            "(#{userId}, #{table_id}, #{category}, #{schoolId}, #{schoolHead}, #{schoolName}, #{schoolProvince}, " +
            "#{rankList}, #{majorId}, #{majorName}, #{firstChoice}, #{otherChoice}, #{special}, #{count}, " +
            "#{year}, #{minScore}, #{minRanking}, #{scoreThanMe}, #{rankingThanMe})")
    void addVolunteer(Volunteer volunteer);

    /**
     * 判断志愿是否被用户收藏
     *
     * @param majorId 专业id
     * @param userId  用户id
     */
    @Select("select count from tb_volunteer where major_id = #{majorId} and user_id = #{userId}")
    Integer checkVolunteer(int majorId, long userId);

}
