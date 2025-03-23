package a311.college.service;

import a311.college.dto.school.*;
import a311.college.dto.query.school.UserGradeQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.result.PageResult;
import a311.college.vo.school.*;

import java.util.List;

/**
 * 大学相关服务
 */
public interface SchoolService {

    PageResult<SchoolVO> pageSelect(SchoolPageQueryDTO schoolPageQueryDTO);

    void cacheSchool();

    List<SchoolVO> getSchoolByName(String schoolName);

    List<SchoolVO> getSchoolByGrade(UserGradeQueryDTO gradeDTO);

    DetailedSchoolVO getDetailSchool(SchoolDTO schoolDTO);

    List<YearScoreVO> scoreLineByYear(YearScoreQueryDTO yearScoreDTO);

    void addSchoolComment(AddSchoolCommentDTO addCommentDTO);

    ForecastVO forecast(ForecastDTO forecastDTO);

    List<BriefSchoolInfoVO> getHotSchool();


}
