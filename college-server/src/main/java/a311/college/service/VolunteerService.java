package a311.college.service;

import a311.college.dto.user.VolunteerPageDTO;
import a311.college.dto.volunteer.AddVolunteerDTO;
import a311.college.entity.volunteer.Volunteer;
import a311.college.entity.volunteer.VolunteerTable;
import a311.college.result.PageResult;
import a311.college.vo.volunteer.SchoolVolunteer;

import java.util.List;

public interface VolunteerService {

    PageResult<SchoolVolunteer> showVolunteer(VolunteerPageDTO volunteerPageDTO);

    void createVolunteerTable(VolunteerTable volunteerTable);

    void deleteVolunteerTable(Integer tableId);

    void updateVolunteerTableName(VolunteerTable volunteerTable);

    List<VolunteerTable> selectTables(Long userId);

    void addVolunteer(AddVolunteerDTO addVolunteerDTO);

    void deleteVolunteer(Volunteer volunteer);

    List<Volunteer> selectVolunteer(Integer tableId);
}
