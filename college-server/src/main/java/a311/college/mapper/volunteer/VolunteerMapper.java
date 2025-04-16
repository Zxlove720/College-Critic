package a311.college.mapper.volunteer;

import a311.college.dto.user.UserVolunteerPageDTO;
import a311.college.vo.volunteer.SchoolVolunteer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VolunteerMapper {

    /**
     * 查询志愿学校
     *
     * @param userVolunteerPageDTO 用户志愿分页查询DTO
     * @return List<School>
     */
    List<SchoolVolunteer> selectVolunteerSchool(UserVolunteerPageDTO userVolunteerPageDTO);
}
