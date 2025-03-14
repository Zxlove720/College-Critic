package a311.college.controller.college;


import a311.college.constant.API.APIConstant;
import a311.college.dto.college.AddCommentDTO;
import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.dto.query.school.GradeDTO;
import a311.college.dto.query.school.SchoolNameDTO;
import a311.college.dto.query.school.YearScoreDTO;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.service.CollegeService;
import a311.college.thread.ThreadLocalUtil;
import a311.college.vo.CollegeSimpleVO;
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

    @Autowired
    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    /**
     * 大学信息分页查询
     *
     * @param collegePageQueryDTO 大学分页查询DTO
     * @return Result<PageResult < School>>
     */
    @PostMapping("/page")
    @Operation(summary = "大学信息分页查询")
    public Result<PageResult<CollegeSimpleVO>> collegeList(@RequestBody CollegePageQueryDTO collegePageQueryDTO) {
        log.info("大学分页查询...查询参数为：第{}页，每页{}条", collegePageQueryDTO.getPage(), collegePageQueryDTO.getPageSize());
        PageResult<CollegeSimpleVO> pageResult = collegeService.pageSelect(collegePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据学校名搜索
     *
     * @param schoolName 学校名
     * @return Result<List < CollegeSimpleVO>>
     */
    @PostMapping("/name")
    @Operation(summary = "根据学校名搜索大学")
    public Result<List<CollegeSimpleVO>> getCollegeByName(@RequestBody SchoolNameDTO schoolName) {
        return collegeService.getCollegeByName(schoolName.getSchoolName());
    }

    /**
     * 获取某一院校的历年分数线
     *
     * @return Result<YearScoreVO>
     */
    @PostMapping("/years")
    @Operation(summary = "获取某院校的历年分数线")
    public Result<List<YearScoreVO>> collegeScoreByYear(@RequestBody YearScoreDTO yearScoreDTO) {
        return collegeService.getScoreByYear(yearScoreDTO);
    }

    /**
     * 根据用户成绩查询大学
     *
     * @return Result<CollegeVO>
     */
    @PostMapping("grade")
    @Operation(summary = "根据用户成绩查询大学")
    public Result<List<CollegeSimpleVO>> getByUserScore(@RequestBody GradeDTO gradeDTO) {
        log.info("用户成绩为：{}", gradeDTO.getGrade());
        List<CollegeSimpleVO> collegeList = collegeService.getByGrade(gradeDTO);
        return Result.success(collegeList);
    }

    /**
     * 用户评价大学
     *
     */
    @PostMapping("/comment")
    @Operation(summary = "用户评价大学")
    public Result<Void> addCollegeComment(@RequestBody AddCommentDTO addCommentDTO) {
        log.info("用户'{}'发表对于'{}'大学的评论", ThreadLocalUtil.getCurrentId(), addCommentDTO.getSchoolId());
        collegeService.addComment(addCommentDTO);
        return Result.success();
    }
}
