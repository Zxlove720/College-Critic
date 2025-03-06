package a311.college.controller.college;


import a311.college.address.AddressToEnumUtil;
import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.service.CollegeService;
import a311.college.vo.CollegeSimpleVO;
import a311.college.vo.CollegeVO;
import a311.college.vo.YearScoreVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 大学请求控制器
 */
@Slf4j
@RestController
@RequestMapping("/colleges")
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
     * @return Result<PageResult<School>>
     */
    @GetMapping("/page")
    public Result<PageResult<CollegeSimpleVO>> collegeList(CollegePageQueryDTO collegePageQueryDTO) {
        log.info("大学分页查询...查询参数为：第{}页，每页{}条", collegePageQueryDTO.getPage(), collegePageQueryDTO.getPageSize());
        PageResult<CollegeSimpleVO> pageResult = collegeService.pageSelect(collegePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 获取某一院校的历年分数线
     * @param id 学校id
     * @param province 招生省份
     * @param year 招生年份
     * @return Result<YearScoreVO>
     */
    @GetMapping("/years")
    public Result<List<YearScoreVO>> collegeScoreByYear(int id, String province, String year) {
        return collegeService.getScoreByYear(id, province, year);
    }

    /**
     * 大学得分
     * @param schoolName 大学名
     * @return Result<Void>
     */
    @GetMapping("/score")
    public Result<Integer> getCollegeScore(String schoolName) {
        CollegeVO school = collegeService.getSchoolByName(schoolName);
        return Result.success(AddressToEnumUtil.toProvinceEnum
                (AddressToEnumUtil.extractProvince(school.getSchoolAddress())).getScore());
    }

    /**
     * 根据用户成绩查询大学
     * @return Result<CollegeVO>
     */
    @GetMapping("grade")
    public Result<List<CollegeVO>> getByUserScore(int grade, String province) {
        log.info("用户成绩为：{}", grade);
        List<CollegeVO> collegeList = collegeService.getByGrade(grade, province);
        return Result.success(collegeList);
    }

    /**
     * 根据省份查询大学
     * @param province 省份名
     * @return Result<List<CollegeVO>>
     */
    @GetMapping("address")
    public Result<List<CollegeSimpleVO>> getByAddress(String province) {
        log.info("查询省份为:{}", province);
        return Result.success(collegeService.getByAddress(province));
    }
}
