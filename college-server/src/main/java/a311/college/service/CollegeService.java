package a311.college.service;

import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.dto.query.school.GradeDTO;
import a311.college.dto.query.school.YearScoreDTO;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.vo.CollegeSimpleVO;
import a311.college.vo.MajorVO;
import a311.college.vo.YearScoreVO;

import java.util.List;

/**
 * 大学相关服务
 */
public interface CollegeService {

    PageResult<CollegeSimpleVO> pageSelect(CollegePageQueryDTO collegePageQueryDTO);

    void cacheCollege();

    List<CollegeSimpleVO> getByGrade(GradeDTO gradeDTO);

    Result<List<YearScoreVO>> getScoreByYear(YearScoreDTO yearScoreDTO);

    Result<List<CollegeSimpleVO>> getCollegeByName(String schoolName);

}
