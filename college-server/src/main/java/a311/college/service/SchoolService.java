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

    PageResult<SchoolSimpleVO> pageSelect(SchoolPageQueryDTO schoolPageQueryDTO);

    void cacheSchool();

    List<SchoolSimpleVO> getSchoolByName(String schoolName);

    List<SchoolSimpleVO> getSchoolByGrade(UserGradeQueryDTO gradeDTO);

    SchoolVO getDetailSchool(SchoolDTO schoolDTO);

    List<YearScoreVO> scoreLineByYear(YearScoreQueryDTO yearScoreDTO);

    void addSchoolComment(AddSchoolCommentDTO addCommentDTO);

    ForecastVO forecast(ForecastDTO forecastDTO);

    List<BriefSchoolInfoVO> getHotSchool(HotSchoolDTO hotSchoolDTO);
}
