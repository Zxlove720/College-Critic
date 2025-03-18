package a311.college.controller.major;

import a311.college.constant.API.APIConstant;
import a311.college.dto.major.MajorQueryDTO;
import a311.college.dto.query.major.MajorNameDTO;
import a311.college.result.Result;
import a311.college.service.MajorService;
import a311.college.vo.MajorVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专业Controller
 */
@RestController
@RequestMapping("/majors")
@Tag(name = APIConstant.MAJOR_SERVICE)
public class MajorController {

    private final MajorService majorService;

    @Autowired
    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    /**
     * 专业查询
     * @param majorDTO 专业查询DTO
     * @return Result<List<MajorVO>>
     */
    @PostMapping
    @Operation(summary = "专业查询")
    public Result<List<MajorVO>> getMajors(@RequestBody MajorQueryDTO majorDTO) {
        return Result.success(majorService.getMajors(majorDTO));
    }

    /**
     * 根据专业名搜索
     *
     * @return Result<List<MajorVO>>
     */
    @PostMapping("/major")
    @Operation(summary = "专业名搜索")
    public Result<List<MajorVO>> getMajorByName(@RequestBody MajorNameDTO majorNameDTO) {
        return majorService.getMajorByName(majorNameDTO.getMajorName());
    }

}
