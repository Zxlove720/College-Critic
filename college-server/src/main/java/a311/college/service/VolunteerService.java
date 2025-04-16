package a311.college.service;

import a311.college.dto.user.UserVolunteerPageDTO;
import a311.college.result.PageResult;
import a311.college.vo.volunteer.SchoolVolunteer;

public interface VolunteerService {

    PageResult<SchoolVolunteer> showVolunteer(UserVolunteerPageDTO userVolunteerPageDTO);
}
