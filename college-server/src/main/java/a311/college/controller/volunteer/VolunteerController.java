package a311.college.controller.volunteer;

import a311.college.dto.user.UserVolunteerPageDTO;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.service.VolunteerService;
import a311.college.vo.volunteer.SchoolVolunteer;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 志愿控制器
 */
@RestController
@RequestMapping("/volunteers")
public class VolunteerController {


    private final VolunteerService volunteerService;

    @Autowired
    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    /**
     * 志愿展示
     *
     * @param userVolunteerPageDTO
     * @return
     */
    @PostMapping("/showVolunteer")
    @Operation(summary = "展示志愿")
    public Result<PageResult<SchoolVolunteer>> showVolunteer(@RequestBody UserVolunteerPageDTO userVolunteerPageDTO) {
        return Result.success(volunteerService.showVolunteer(userVolunteerPageDTO));
    }
}
