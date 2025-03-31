package a311.college.controller.major;

import a311.college.constant.API.APIConstant;
import a311.college.dto.ai.MajorAIRequestDTO;
import a311.college.dto.query.major.MajorPageQueryDTO;
import a311.college.dto.query.major.MajorNameQueryDTO;
import a311.college.entity.major.Major;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.service.DeepSeekService;
import a311.college.service.MajorService;
import a311.college.vo.ai.MajorAIMessageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专业Controller
 */
@Slf4j
@RestController
@RequestMapping("/majors")
@Tag(name = APIConstant.MAJOR_SERVICE)
public class MajorController {

    private final MajorService majorService;

    private final DeepSeekService deepSeekService;

    @Autowired
    public MajorController(MajorService majorService, DeepSeekService deepSeekService) {
        this.majorService = majorService;
        this.deepSeekService = deepSeekService;
    }

    /**
     * 专业分页查询
     *
     * @param majorPageQueryDTO 专业分页查询DTO
     * @return Result<PageResult < Major>>
     */
    @PostMapping("/page")
    @Operation(summary = "专业分页查询")
    public Result<PageResult<Major>> majorPageQuery(@RequestBody MajorPageQueryDTO majorPageQueryDTO) {
        log.info("专业分页查询...");
        return Result.success(majorService.majorPageQuery(majorPageQueryDTO));
    }

    /**
     * 根据专业名搜索
     *
     * @return Result<List < MajorVO>>
     */
    @PostMapping("/major")
    @Operation(summary = "专业名搜索")
    public Result<List<Major>> searchMajorByName(@RequestBody MajorNameQueryDTO majorNameDTO) {
        log.info("专业名搜索'{}'", majorNameDTO.getMajorName());
        return Result.success(majorService.searchMajorByName(majorNameDTO.getMajorName()));
    }

    /**
     * 请求AI获取专业信息
     *
     * @return Result<MajorAIMessageVO>
     */
    @PostMapping("/information")
    @Operation(summary = "请求AI获取专业信息")
    public Result<MajorAIMessageVO> majorAIRequest(@RequestBody MajorAIRequestDTO majorAIRequestDTO) {
        log.info("正在请求'{}'专业的信息", majorAIRequestDTO.getMajorId());
        MajorAIMessageVO majorAIMessageVO = deepSeekService.majorInformation(majorAIRequestDTO);
        return Result.success(majorAIMessageVO);
    }

}
