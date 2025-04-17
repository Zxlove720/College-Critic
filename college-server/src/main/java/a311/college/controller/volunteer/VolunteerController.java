package a311.college.controller.volunteer;

import a311.college.dto.user.VolunteerPageDTO;
import a311.college.dto.volunteer.AddVolunteerDTO;
import a311.college.entity.volunteer.VolunteerTable;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.service.VolunteerService;
import a311.college.vo.volunteer.SchoolVolunteer;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 志愿控制器
 */
@Slf4j
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
     * @param volunteerPageDTO 志愿分页查询DTO
     * @return Result<PageResult<SchoolVolunteer>>
     */
    @PostMapping("/showVolunteer")
    @Operation(summary = "展示志愿")
    public Result<PageResult<SchoolVolunteer>> showVolunteer(@RequestBody VolunteerPageDTO volunteerPageDTO) {
        return Result.success(volunteerService.showVolunteer(volunteerPageDTO));
    }

    /**
     * 创建志愿表
     *
     * @param volunteerTable 志愿表实体类
     * @return Result<Void>
     */
    @PostMapping("/create")
    @Operation(summary = "创建志愿表")
    public Result<Void> createVolunteerTable(@RequestBody VolunteerTable volunteerTable) {
        log.info("创建志愿表");
        volunteerService.createVolunteerTable(volunteerTable);
        return Result.success();
    }


    /**
     * 添加志愿
     *
     * @param addVolunteerDTO 添加志愿DTO
     */
    @PostMapping("/addVolunteer")
    @Operation(summary = "添加志愿")
    public Result<Void> addVolunteer(@RequestBody AddVolunteerDTO addVolunteerDTO) {
        volunteerService.addVolunteer(addVolunteerDTO);
        return Result.success();
    }
}
