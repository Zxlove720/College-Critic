package a311.college.controller.school;


import a311.college.constant.API.APIConstant;
import a311.college.dto.school.*;
import a311.college.dto.query.school.UserGradeQueryDTO;
import a311.college.dto.query.school.SchoolNameQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.entity.school.School;
import a311.college.entity.school.SchoolMajor;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.service.SchoolService;
import a311.college.service.DeepSeekService;
import a311.college.thread.ThreadLocalUtil;
import a311.college.vo.school.*;
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
@RequestMapping("/schools")
@Tag(name = APIConstant.SCHOOL_SERVICE)
public class SchoolController {

    private final SchoolService schoolService;

    private final DeepSeekService deepSeekService;

    @Autowired
    public SchoolController(SchoolService schoolService, DeepSeekService deepSeekService) {
        this.schoolService = schoolService;
        this.deepSeekService = deepSeekService;
    }

    /**
     * 大学信息分页查询
     *
     * @param schoolPageQueryDTO 大学分页查询DTO
     * @return Result<PageResult < School>>
     */
    @PostMapping("/page")
    @Operation(summary = "大学分页查询")
    public Result<PageResult<School>> schoolPageSelect(@RequestBody SchoolPageQueryDTO schoolPageQueryDTO) {
        log.info("大学分页查询...查询参数为：第{}页，每页{}条", schoolPageQueryDTO.getPage(), schoolPageQueryDTO.getPageSize());
        PageResult<School> pageResult = schoolService.pageSelect(schoolPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 大学专业分页查询
     *
     * @param schoolMajorPageDTO 大学专业分页查询DTO
     * @return Result<PageResult<SchoolMajor>>
     */
    @PostMapping("/majors")
    @Operation(summary = "大学专业分页查询")
    public Result<PageResult<SchoolMajor>> schoolMajorPageSelect(@RequestBody SchoolMajorPageDTO schoolMajorPageDTO) {
        log.info("大学专业分页查询...查询参数为：第{}页，每页{}条", schoolMajorPageDTO.getPage(), schoolMajorPageDTO.getPageSize());
        PageResult<SchoolMajor> pageResult = schoolService.pageSelectMajor(schoolMajorPageDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据学校名搜索大学
     *
     * @param schoolNameQueryDTO 学校名DTO
     * @return Result<List < SchoolVO>>
     */
    @PostMapping("/name")
    @Operation(summary = "根据学校名搜索大学")
    public Result<List<School>> searchSchool(@RequestBody SchoolNameQueryDTO schoolNameQueryDTO) {
        return Result.success(schoolService.searchSchool(schoolNameQueryDTO));
    }

    /**
     * 根据用户成绩查询大学
     *
     * @param gradeDTO 成绩查询DTO
     * @return List<School>
     */
    @PostMapping("grade")
    @Operation(summary = "根据用户成绩查询大学")
    public Result<List<School>> getByUserScore(@RequestBody UserGradeQueryDTO gradeDTO) {
        log.info("用户成绩为：{}", gradeDTO.getGrade());
        return Result.success(schoolService.getSchoolByGrade(gradeDTO));
    }

    /**
     * 查询大学具体信息
     *
     * @param schoolDTO 大学查询DTO
     * @return DetailedSchoolVO 大学具体信息
     */
    @PostMapping("/school")
    @Operation(summary = "查询大学具体信息")
    public Result<DetailedSchoolVO> getDetailSchool(@RequestBody SchoolDTO schoolDTO) {
        DetailedSchoolVO detailedSchoolVO = schoolService.getDetailSchool(schoolDTO);
        return Result.success(detailedSchoolVO);
    }

    /**
     * 录取预测
     *
     * @param forecastDTO 录取预测DTO
     * @return ForecastVO 录取预测VO
     */
    @PostMapping("/forecast")
    public Result<ForecastVO> forecast(@RequestBody ForecastDTO forecastDTO) {
        log.info("用户'{}'正在做录取预测", ThreadLocalUtil.getCurrentId());
        return Result.success(schoolService.forecast(forecastDTO));
    }

    /**
     * 获取某一院校的历年分数线
     *
     * @param yearScoreDTO 分数线查询DTO
     * @return List<YearScoreVO>
     */
    @PostMapping("/scoreLine")
    @Operation(summary = "获取某院校的历年分数线")
    public Result<List<YearScoreVO>> getScoreLine(@RequestBody YearScoreQueryDTO yearScoreDTO) {
        return Result.success(schoolService.scoreLineByYear(yearScoreDTO));
    }

    /**
     * 用户评价大学
     *
     * @param addCommentDTO 评价DTO
     */
    @PostMapping("/comment")
    @Operation(summary = "用户评价大学")
    public Result<Void> addSchoolComment(@RequestBody AddSchoolCommentDTO addCommentDTO) {
        log.info("用户'{}'发表对于'{}'大学的评论", ThreadLocalUtil.getCurrentId(), addCommentDTO.getSchoolId());
        schoolService.addSchoolComment(addCommentDTO);
        return Result.success();
    }

    /**
     * 获取热门院校
     *
     * @return Result<List<BriefSchoolInfoVO>>
     */
    @PostMapping("/hot")
    public Result<List<BriefSchoolInfoVO>> hotSchool() {
        log.info("获取热门院校");
        List<BriefSchoolInfoVO>schoolSimpleVOList = schoolService.getHotSchool();
        return Result.success(schoolSimpleVOList);
    }

}
