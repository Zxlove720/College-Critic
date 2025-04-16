package a311.college.mapper.volunteer;

import a311.college.dto.user.VolunteerPageDTO;
import a311.college.entity.school.SchoolMajor;
import a311.college.vo.volunteer.SchoolVolunteer;
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
    @Select("select * from tb_score where major_id = #{majorId}")
    SchoolMajor selectSchoolMajorById(Integer majorId);
}
