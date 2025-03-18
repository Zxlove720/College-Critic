package a311.college.service;

import a311.college.dto.school.AddSchoolCommentDTO;
import a311.college.dto.school.SchoolDTO;
import a311.college.dto.school.SchoolPageQueryDTO;
import a311.college.dto.query.school.UserGradeQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.result.PageResult;
import a311.college.vo.SchoolSimpleVO;
import a311.college.vo.SchoolVO;
import a311.college.vo.YearScoreVO;

import java.util.List;

/**
 * 大学相关服务
 */
public interface CollegeService {

    PageResult<SchoolSimpleVO> pageSelect(SchoolPageQueryDTO collegePageQueryDTO);

    void cacheCollege();

    List<SchoolSimpleVO> getCollegeByName(String schoolName);

    List<SchoolSimpleVO> getByGrade(UserGradeQueryDTO gradeDTO);

    List<YearScoreVO> getScoreByYear(YearScoreQueryDTO yearScoreDTO);

    void addComment(AddSchoolCommentDTO addCommentDTO);

    void addScore();

    SchoolVO getCollege(SchoolDTO collegeDTO);
}
