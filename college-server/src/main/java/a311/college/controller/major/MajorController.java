package a311.college.controller.major;

import a311.college.dto.major.MajorDTO;
import a311.college.result.Result;
import a311.college.service.MajorService;
import a311.college.vo.MajorVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 专业Controller
 */
@RestController
@RequestMapping("/majors")
public class MajorController {

    private final MajorService majorService;

    @Autowired
    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    /**
     * 专业分页查询
     * @param majorDTO 专业查询DTO
     * @return Result<List<MajorVO>>
     */
    @GetMapping
    public Result<List<MajorVO>> getMajors(MajorDTO majorDTO) {
        return Result.success(majorService.getMajors(majorDTO));
    }

}
