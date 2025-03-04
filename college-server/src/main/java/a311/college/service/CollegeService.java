package a311.college.service;

import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.vo.CollegeSimpleVO;
import a311.college.vo.CollegeVO;
import a311.college.vo.YearScoreVO;

import java.util.List;

/**
 * 大学相关服务
 */
public interface CollegeService {

    PageResult<CollegeSimpleVO> pageSelect(CollegePageQueryDTO collegePageQueryDTO);

    CollegeVO getSchoolByName(String schoolName);

    List<CollegeVO> getByGrade(int grade, String province);

    List<CollegeVO> getByAddress(String province);

    Result<List<YearScoreVO>> getScoreByYear(int id, String province, String year);
}
