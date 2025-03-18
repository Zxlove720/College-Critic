package a311.college.controller.college;


import a311.college.constant.API.APIConstant;
import a311.college.dto.school.AddSchoolCommentDTO;
import a311.college.dto.school.SchoolDTO;
import a311.college.dto.school.SchoolPageQueryDTO;
import a311.college.dto.query.school.UserGradeQueryDTO;
import a311.college.dto.query.school.SchoolNameQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.service.CollegeService;
import a311.college.service.DeepSeekService;
import a311.college.thread.ThreadLocalUtil;
import a311.college.vo.SchoolSimpleVO;
import a311.college.vo.SchoolVO;
import a311.college.vo.YearScoreVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 大学请求控制器
 */
@Slf4j
@RestController
@RequestMapping("/colleges")
@Tag(name = APIConstant.COLLEGE_SERVICE)
public class CollegeController {

    private final CollegeService collegeService;

    private final DeepSeekService deepSeekService;

    @Autowired
    public CollegeController(CollegeService collegeService, DeepSeekService deepSeekService) {
        this.collegeService = collegeService;
        this.deepSeekService = deepSeekService;
    }

    /**
     * 大学信息分页查询
     *
     * @param collegePageQueryDTO 大学分页查询DTO
     * @return Result<PageResult < School>>
     */
    @PostMapping("/page")
    @Operation(summary = "大学信息分页查询")
    public Result<PageResult<SchoolSimpleVO>> collegeList(@RequestBody SchoolPageQueryDTO collegePageQueryDTO) {
        log.info("大学分页查询...查询参数为：第{}页，每页{}条", collegePageQueryDTO.getPage(), collegePageQueryDTO.getPageSize());
        PageResult<SchoolSimpleVO> pageResult = collegeService.pageSelect(collegePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据学校名搜索大学
     *
     * @param schoolName 学校名DTO
     * @return Result<List < SchoolSimpleVO>>
     */
    @PostMapping("/name")
    @Operation(summary = "根据学校名搜索大学")
    public Result<List<SchoolSimpleVO>> getCollegeByName(@RequestBody SchoolNameQueryDTO schoolName) {
        return Result.success(collegeService.getCollegeByName(schoolName.getSchoolName()));
    }

    /**
     * 根据用户成绩查询大学
     *
     * @param gradeDTO 成绩查询DTO
     * @return List<SchoolSimpleVO>
     */
    @PostMapping("grade")
    @Operation(summary = "根据用户成绩查询大学")
    public Result<List<SchoolSimpleVO>> getByUserScore(@RequestBody UserGradeQueryDTO gradeDTO) {
        log.info("用户成绩为：{}", gradeDTO.getGrade());
        return Result.success(collegeService.getByGrade(gradeDTO));
    }

    /**
     * 查询院校具体信息
     *
     * @param collegeDTO 大学查询DTO
     * @return SchoolVO 大学具体信息
     */
    @PostMapping("/college")
    @Operation(summary = "查询院校具体信息")
    public Result<SchoolVO> getCollege(@RequestBody SchoolDTO collegeDTO) {
        SchoolVO collegeVO = collegeService.getCollege(collegeDTO);
        return Result.success(collegeVO);
    }

    /**
     * 获取某一院校的历年分数线
     *
     * @param yearScoreDTO 年份分数线DTO
     * @return List<YearScoreVO>
     */
    @PostMapping("/years")
    @Operation(summary = "获取某院校的历年分数线")
    public Result<List<YearScoreVO>> collegeScoreByYear(@RequestBody YearScoreQueryDTO yearScoreDTO) {
        return Result.success(collegeService.getScoreByYear(yearScoreDTO));
    }

    /**
     * 用户评价大学
     *
     * @param addCommentDTO 评价DTO
     * @return Void
     */
    @PostMapping("/comment")
    @Operation(summary = "用户评价大学")
    public Result<Void> addCollegeComment(@RequestBody AddSchoolCommentDTO addCommentDTO) {
        log.info("用户'{}'发表对于'{}'大学的评论", ThreadLocalUtil.getCurrentId(), addCommentDTO.getSchoolId());
        collegeService.addComment(addCommentDTO);
        return Result.success();
    }

    /**
     * 改变大学数据
     *
     */
    @PostMapping("/addScore")
    public Result<Void> addScore2College() {
        collegeService.addScore();
        return Result.success();
    }
}
