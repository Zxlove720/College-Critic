package a311.college.service;

import a311.college.dto.query.PageQueryDTO;
import a311.college.dto.query.school.*;
import a311.college.dto.school.*;
import a311.college.dto.user.UserSearchDTO;
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

    List<SchoolVO> searchSchool(SchoolNameQueryDTO schoolNameQueryDTO);

    SearchVO search(UserSearchDTO userSearchDTO);

    PageResult<SchoolVO> getSchoolByGrade(GradePageQueryDTO gradePageQueryDTO);

    DetailedSchoolVO getDetailSchool(SchoolDTO schoolDTO);

    void addFavoriteSchool(SchoolDTO schoolDTO);

    void deleteFavoriteSchool(SchoolDTO schoolDTO);

    ForecastVO forecast(ForecastDTO forecastDTO);

    List<YearScoreVO> scoreLineByYear(YearScoreQueryDTO yearScoreDTO);

    void addSchoolComment(AddSchoolCommentDTO addCommentDTO);

    PageResult<CommentVO> showComment(SchoolCommentPageQueryDTO schoolCommentPageQueryDTO);

    List<SchoolVO> getSchool1(ProvinceQueryDTO provinceQueryDTO);

    List<SchoolVO> getSchool2(ProvinceQueryDTO provinceQueryDTO);

    List<SchoolVO> getSchool3(ProvinceQueryDTO provinceQueryDTO);

    List<SchoolVO> getSchool4(ProvinceQueryDTO provinceQueryDTO);

    List<HotMajorVO> getHotMajor();

    List<HotMajorVO> getHotMajorProfessional();

    List<SchoolVO> getHotSchool();

    List<SchoolVO> getBasicSchool();

    List<SchoolSceneryVO> getScenery();

}
