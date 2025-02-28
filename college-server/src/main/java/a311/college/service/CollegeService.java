package a311.college.service;

import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.result.PageResult;
import a311.college.vo.CollegeVO;

import java.util.List;

/**
 * 大学相关服务
 */
public interface CollegeService {

    PageResult<CollegeVO> pageSelect(CollegePageQueryDTO collegePageQueryDTO);

    CollegeVO getSchoolByName(String schoolName);

    List<CollegeVO> getByGrade(int grade, String province);

}
