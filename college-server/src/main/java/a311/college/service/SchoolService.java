package a311.college.service;

import a311.college.dto.query.school.SchoolNameQueryDTO;
import a311.college.dto.school.*;
import a311.college.dto.query.school.UserGradeQueryDTO;
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

    PageResult<School> pageSelect(SchoolPageQueryDTO schoolPageQueryDTO);

    PageResult<SchoolMajor> pageSelectMajor(SchoolMajorPageDTO schoolMajorPageDTO);

    void cacheSchool();

    List<School> searchSchool(SchoolNameQueryDTO schoolNameQueryDTO);

    SearchVO search(UserSearchDTO userSearchDTO);

    List<School> getSchoolByGrade(UserGradeQueryDTO gradeDTO);

    DetailedSchoolVO getDetailSchool(SchoolDTO schoolDTO);

    void addFavoriteSchool(SchoolDTO schoolDTO);

    ForecastVO forecast(ForecastDTO forecastDTO);

    List<YearScoreVO> scoreLineByYear(YearScoreQueryDTO yearScoreDTO);

    void addSchoolComment(AddSchoolCommentDTO addCommentDTO);

    void deleteFavoriteSchool(SchoolDTO schoolDTO);

    List<CommentVO> showComment(SchoolDTO schoolDTO);

    List<BriefSchoolInfoVO> getHotSchool();

    List<HotMajorVO> getHotMajor();

    List<HotMajorVO> getHotMajorProfessional();

    List<SchoolSceneryVO> getScenery();
}
