package a311.college.service;

import a311.college.dto.college.AddCommentDTO;
import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.dto.query.school.GradeDTO;
import a311.college.dto.query.school.YearScoreDTO;
import a311.college.result.PageResult;
import a311.college.vo.CollegeSimpleVO;
import a311.college.vo.YearScoreVO;

import java.util.List;

/**
 * 大学相关服务
 */
public interface CollegeService {

    PageResult<CollegeSimpleVO> pageSelect(CollegePageQueryDTO collegePageQueryDTO);

    void cacheCollege();

    List<CollegeSimpleVO> getCollegeByName(String schoolName);

    List<CollegeSimpleVO> getByGrade(GradeDTO gradeDTO);

    List<YearScoreVO> getScoreByYear(YearScoreDTO yearScoreDTO);

    void addComment(AddCommentDTO addCommentDTO);

    void addScore();

}
