package a311.college.service;

import a311.college.dto.user.VolunteerPageDTO;
import a311.college.dto.volunteer.AddVolunteerDTO;
import a311.college.result.PageResult;
import a311.college.vo.volunteer.SchoolVolunteer;

public interface VolunteerService {

    PageResult<SchoolVolunteer> showVolunteer(VolunteerPageDTO volunteerPageDTO);

    void addVolunteer(AddVolunteerDTO addVolunteerDTO);

}
