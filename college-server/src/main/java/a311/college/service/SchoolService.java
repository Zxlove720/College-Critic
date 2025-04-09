package a311.college.service;

import a311.college.dto.query.PageQueryDTO;
import a311.college.dto.query.school.SchoolCommentPageQueryDTO;
import a311.college.dto.query.school.SchoolNameQueryDTO;
import a311.college.dto.school.*;
import a311.college.dto.query.school.GradePageQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.dto.user.UserSearchDTO;
import a311.college.entity.school.School;
import a311.college.entity.school.SchoolMajor;
import a311.college.result.PageResult;
import a311.college.vo.major.HotMajorVO;
import a311.college.vo.school.*;

import java.util.List;

/**
 * 大学相关服务
 */
public interface SchoolService {

    PageResult<SchoolVO> pageSelect(SchoolPageQueryDTO schoolPageQueryDTO);

    PageResult<SchoolMajor> pageSelectMajor(SchoolMajorPageQueryDTO schoolMajorPageQueryDTO);

    void cacheSchool();

    List<School> searchSchool(SchoolNameQueryDTO schoolNameQueryDTO);

    SearchVO search(UserSearchDTO userSearchDTO);

    PageResult<School> getSchoolByGrade(GradePageQueryDTO gradePageQueryDTO);

    DetailedSchoolVO getDetailSchool(SchoolDTO schoolDTO);

    void addFavoriteSchool(SchoolDTO schoolDTO);

    void deleteFavoriteSchool(SchoolDTO schoolDTO);

    ForecastVO forecast(ForecastDTO forecastDTO);

    List<YearScoreVO> scoreLineByYear(YearScoreQueryDTO yearScoreDTO);

    void addSchoolComment(AddSchoolCommentDTO addCommentDTO);

    PageResult<CommentVO> showComment(SchoolCommentPageQueryDTO schoolCommentPageQueryDTO);

    List<HotMajorVO> getHotMajor();

    List<HotMajorVO> getHotMajorProfessional();

    PageResult<School> getClassicSchool(PageQueryDTO pageQueryDTO);

    List<HotSchoolVO> getHotSchool();

    List<SchoolSceneryVO> getScenery();

}
