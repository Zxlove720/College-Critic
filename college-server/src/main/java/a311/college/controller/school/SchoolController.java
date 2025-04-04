package a311.college.controller.school;


import a311.college.constant.API.APIConstant;
import a311.college.dto.ai.SchoolAIRequestDTO;
import a311.college.dto.school.*;
import a311.college.dto.query.school.UserGradeQueryDTO;
import a311.college.dto.query.school.SchoolNameQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.dto.user.UserSearchDTO;
import a311.college.vo.ai.SchoolAIMessageVO;
import a311.college.entity.school.School;
import a311.college.entity.school.SchoolMajor;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.service.SchoolService;
import a311.college.service.DeepSeekService;
import a311.college.thread.ThreadLocalUtil;
import a311.college.vo.major.HotMajorVO;
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
     * @return Result<PageResult < SchoolMajor>>
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
    @PostMapping("/search")
    @Operation(summary = "根据学校名搜索大学")
    public Result<List<School>> searchSchool(@RequestBody SchoolNameQueryDTO schoolNameQueryDTO) {
        return Result.success(schoolService.searchSchool(schoolNameQueryDTO));
    }

    /**
     * 用户搜索
     *
     * @param userSearchDTO 用户搜索DTO
     * @return Result<SearchVO>
     */
    @PostMapping("/searchList")
    @Operation(summary = "用户搜索")
    public Result<SearchVO> search(@RequestBody UserSearchDTO userSearchDTO) {
        log.info("用户正在搜索：{}", userSearchDTO.getMessage());
        SearchVO searchVO = schoolService.search(userSearchDTO);
        return Result.success(searchVO);
    }

    /**
     * 根据用户成绩查询大学
     *
     * @param gradeDTO 成绩查询DTO
     * @return List<School>
     */
    @PostMapping("grade")
    @Operation(summary = "根据用户成绩查询大学")
    // TODO查询十分缓慢
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
     * 用户收藏大学
     *
     * @param schoolDTO 大学DTO
     */
    @PostMapping("/addSchool")
    @Operation(summary = "用户收藏学校")
    public Result<Void> addFavoriteSchool(@RequestBody SchoolDTO schoolDTO) {
        log.info("用户'{}'收藏了'{}'学校", ThreadLocalUtil.getCurrentId(), schoolDTO.getSchoolId());
        schoolService.addFavoriteSchool(schoolDTO);
        return Result.success();
    }

    /**
     * 用户删除收藏
     *
     * @param schoolDTO 大学DTO
     */
    @PostMapping("/deleteFavorite")
    @Operation(summary = "用户删除收藏")
    public Result<Void> deleteFavoriteSchool(@RequestBody SchoolDTO schoolDTO) {
        log.info("用户'{}'删除收藏'{}'学校", ThreadLocalUtil.getCurrentId(), schoolDTO.getSchoolId());
        schoolService.deleteFavoriteSchool(schoolDTO);
        return Result.success();
    }

    /**
     * 录取预测
     *
     * @param forecastDTO 录取预测DTO
     * @return ForecastVO 录取预测VO
     */
    @PostMapping("/forecast")
    @Operation(summary = "录取预测")
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
     * 获取用户评价
     *
     * @param schoolDTO 大学DTO
     * @return Result<List < CommentVO>>
     */
    @PostMapping("/showComment")
    @Operation(summary = "获取用户评价")
    public Result<List<CommentVO>> showSchoolComment(@RequestBody SchoolDTO schoolDTO) {
        log.info("查看大学'{}'的评价", schoolDTO.getSchoolId());
        List<CommentVO> commentVOList = schoolService.showComment(schoolDTO);
        return Result.success(commentVOList);
    }

    /**
     * 获取热门院校
     *
     * @return Result<List < BriefSchoolInfoVO>>
     */
    @PostMapping("/hotSchool")
    @Operation(summary = "获取热门院校")
    public Result<List<BriefSchoolInfoVO>> hotSchool() {
        log.info("获取热门院校");
        List<BriefSchoolInfoVO> schoolSimpleVOList = schoolService.getHotSchool();
        return Result.success(schoolSimpleVOList);
    }

    /**
     * 获取热门专业（本科）
     *
     * @return Result<List < HotMajorVO>>
     */
    @PostMapping("/hotMajor1")
    @Operation(summary = "获取热门专业（本科）")
    public Result<List<HotMajorVO>> hotMajor() {
        log.info("获取热门专业（本科）");
        List<HotMajorVO> hotMajorVOList = schoolService.getHotMajor();
        return Result.success(hotMajorVOList);
    }

    /**
     * 获取热门专业（专科）
     *
     * @return Result<List < HotMajorVO>>
     */
    @PostMapping("/hotMajor2")
    @Operation(summary = "获取热门专业（专科）")
    public Result<List<HotMajorVO>> hotMajorProfessional() {
        log.info("获取热门专业（专科）");
        List<HotMajorVO> hotMajorVOList = schoolService.getHotMajorProfessional();
        return Result.success(hotMajorVOList);
    }

    /**
     * 获取首页校园风光
     *
     * @return Result<List < SchoolSceneryVO>>
     */
    @PostMapping("/scenery")
    @Operation(summary = "获取首页校园风光")
    public Result<List<SchoolSceneryVO>> getSchoolScenery() {
        log.info("获取校园风光");
        List<SchoolSceneryVO> schoolSceneryVOList = schoolService.getScenery();
        return Result.success(schoolSceneryVOList);
    }

    /**
     * 请求AI获取学校信息
     *
     * @param schoolAIRequestDTO 大学AI请求 DTO
     * @return Result<SchoolAIMessageVO>
     */
    @PostMapping("/information")
    @Operation(summary = "请求AI获取学校信息")
    public Result<SchoolAIMessageVO> schoolAIRequest(@RequestBody SchoolAIRequestDTO schoolAIRequestDTO) {
        log.info("正在请求'{}'学校的信息", schoolAIRequestDTO.getSchoolId());
        SchoolAIMessageVO schoolAIMessageVO = deepSeekService.schoolInformation(schoolAIRequestDTO);
        return Result.success(schoolAIMessageVO);
    }
}
