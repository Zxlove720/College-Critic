package a311.college.service;

import a311.college.dto.college.AddCollegeCommentDTO;
import a311.college.dto.college.CollegeDTO;
import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.dto.query.college.UserGradeQueryDTO;
import a311.college.dto.query.college.YearScoreQueryDTO;
import a311.college.result.PageResult;
import a311.college.vo.CollegeSimpleVO;
import a311.college.vo.CollegeVO;
import a311.college.vo.YearScoreVO;

import java.util.List;

/**
 * 大学相关服务
 */
public interface CollegeService {

    PageResult<CollegeSimpleVO> pageSelect(CollegePageQueryDTO collegePageQueryDTO);

    void cacheCollege();

    List<CollegeSimpleVO> getCollegeByName(String schoolName);

    List<CollegeSimpleVO> getByGrade(UserGradeQueryDTO gradeDTO);

    List<YearScoreVO> getScoreByYear(YearScoreQueryDTO yearScoreDTO);

    void addComment(AddCollegeCommentDTO addCommentDTO);

    void addScore();

    CollegeVO getCollege(CollegeDTO collegeDTO);
}
