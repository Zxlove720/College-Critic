package a311.college.controller.college;


import a311.college.address.AddressToEnumUtil;
import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.service.CollegeService;
import a311.college.vo.CollegeVO;
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
    public Result<PageResult<CollegeVO>> collegeList(CollegePageQueryDTO collegePageQueryDTO) {
        log.info("大学分页查询...查询参数为：第{}页，每页{}条", collegePageQueryDTO.getPage(), collegePageQueryDTO.getPageSize());
        PageResult<CollegeVO> pageResult = collegeService.pageSelect(collegePageQueryDTO);
        return Result.success(pageResult);
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
                (AddressToEnumUtil.extractProvince(school.getAddress())).getScore());
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

}
