package a311.college.service;

import a311.college.dto.school.AddSchoolCommentDTO;
import a311.college.dto.school.ForecastDTO;
import a311.college.dto.school.SchoolDTO;
import a311.college.dto.school.SchoolPageQueryDTO;
import a311.college.dto.query.school.UserGradeQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.result.PageResult;
import a311.college.vo.school.ForecastVO;
import a311.college.vo.school.SchoolSimpleVO;
import a311.college.vo.school.SchoolVO;
import a311.college.vo.school.YearScoreVO;

import java.util.List;

/**
 * 大学相关服务
 */
public interface SchoolService {

    PageResult<SchoolSimpleVO> pageSelect(SchoolPageQueryDTO schoolPageQueryDTO);

    void cacheSchool();

    List<SchoolSimpleVO> getSchoolByName(String schoolName);

    List<SchoolSimpleVO> getSchoolByGrade(UserGradeQueryDTO gradeDTO);

    SchoolVO getDetailSchool(SchoolDTO schoolDTO);

    List<YearScoreVO> scoreLineByYear(YearScoreQueryDTO yearScoreDTO);

    void addSchoolComment(AddSchoolCommentDTO addCommentDTO);

    void addScore();

    ForecastVO forecast(ForecastDTO forecastDTO);
}
