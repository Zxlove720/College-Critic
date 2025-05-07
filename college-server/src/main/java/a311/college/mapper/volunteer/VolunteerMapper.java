package a311.college.mapper.volunteer;

import a311.college.dto.user.VolunteerPageDTO;
import a311.college.entity.volunteer.Volunteer;
import a311.college.entity.volunteer.VolunteerTable;
import a311.college.vo.volunteer.SchoolVolunteer;
import org.apache.ibatis.annotations.*;

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
     * 判断志愿表是否重复创建
     *
     * @param tableName 志愿表名
     * @param userId    用户id
     */
    @Select("select count(table_id) from tb_volunteer_table where table_name = #{tableName} and user_id = #{userId}")
    Integer checkVolunteerTable(String tableName, long userId);

    /**
     * 根据id查询专业
     *
     * @param majorId 专业id
     */
    Volunteer selectSchoolMajorById(Integer majorId);

    /**
     * 获取志愿顺序
     *
     * @param tableId 志愿表id
     * @return count 顺序
     */
    @Select("select count from tb_volunteer where table_id = #{tableId} order by count desc limit 1")
    Integer getCount(int tableId);

    /**
     * 判断志愿是否被用户收藏
     *
     * @param majorId 专业id
     * @param tableId 志愿表id
     */
    @Select("select count(major_id) from tb_volunteer where major_id = #{majorId} and table_id = #{tableId}")
    int checkVolunteer(int majorId, int tableId);

    /**
     * 创建志愿表
     *
     * @param volunteerTable 志愿表
     */
    @Insert("insert into tb_volunteer_table (user_id, table_name, create_time) VALUES (#{userId}, #{tableName}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "tableId", keyColumn = "table_id")
    void createVolunteerTable(VolunteerTable volunteerTable);

    /**
     * 删除志愿表
     *
     * @param tableId 志愿表id
     */
    @Delete("delete from tb_volunteer_table where table_id = #{tableId}")
    void deleteVolunteerTable(Integer tableId);

    /**
     * 清空志愿表
     *
     * @param tableId 志愿表id
     */
    @Delete("delete from tb_volunteer where table_id = #{tableId}")
    void clearVolunteerTable(Integer tableId);

    /**
     * 更新志愿表名字
     *
     * @param volunteerTable 志愿表
     */
    @Update("update tb_volunteer_table set table_name = #{tableName} where table_id = #{tableId}")
    void updateVolunteerTableName(VolunteerTable volunteerTable);

    /**
     * 查询用户拥有志愿表
     *
     * @param userId 用户id
     * @return List<VolunteerTable>
     */
    @Select("select * from tb_volunteer_table where user_id = #{userId}")
    List<VolunteerTable> selectTables(Long userId);

    /**
     * 查询志愿表已有志愿个数
     *
     * @param tableId 志愿表id
     * @return 志愿个数
     */
    @Select("select max(count) from tb_volunteer where table_id = #{tableId}")
    Integer getTableCount(int tableId);

    /**
     * 添加志愿
     *
     * @param volunteer 志愿实体
     */
    @Insert("insert into tb_volunteer(user_id, table_id, category, school_id, school_head, school_name, " +
            "school_province, rank_list, major_id, major_name, first_choice, other_choice, " +
            "special, count, year, min_score, min_ranking, scoreThanMe, rankingThanMe) values " +
            "(#{userId}, #{tableId}, #{category}, #{schoolId}, #{schoolHead}, #{schoolName}, #{schoolProvince}, " +
            "#{rankList}, #{majorId}, #{majorName}, #{firstChoice}, #{otherChoice}, #{special}, #{count}, " +
            "#{year}, #{minScore}, #{minRanking}, #{scoreThanMe}, #{rankingThanMe})")
    void addVolunteer(Volunteer volunteer);

    /**
     * 删除志愿
     *
     * @param volunteer 志愿实体
     */
    @Delete("delete from tb_volunteer where volunteer_id = #{volunteerId}")
    void deleteVolunteer(Volunteer volunteer);

    /**
     * 查询志愿表内容
     *
     * @param tableId 志愿表id
     * @return List<Volunteer>
     */
    @Select("select * from tb_volunteer where table_id = #{tableId} order by count")
    List<Volunteer> selectVolunteers(Integer tableId);

    /**
     * 返回志愿表顺序
     *
     * @param majorId 专业id
     * @param tableId 志愿表id
     * @return count
     */
    @Select("select count from tb_volunteer where major_id = #{majorId} and table_id = #{tableId}")
    Integer getVolunteerCount(int majorId, int tableId);
}
