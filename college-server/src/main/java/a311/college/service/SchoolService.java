package a311.college.service;

import a311.college.dto.query.school.*;
import a311.college.dto.school.*;
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

    PageResult<SchoolMajor> pageSelectMajor(SchoolMajorPageQueryDTO schoolMajorPageQueryDTO);

    void cacheSchool();

    List<School> searchSchool(SchoolNameQueryDTO schoolNameQueryDTO);

    SearchVO searchList(UserSearchDTO userSearchDTO);

    DetailedSchoolVO getDetailSchool(SchoolDTO schoolDTO);

    void addFavoriteSchool(SchoolDTO schoolDTO);

    void deleteFavoriteSchool(SchoolDTO schoolDTO);

    List<School> getCloseSchool(SchoolDTO schoolDTO);

    SchoolForecastVO forecast(ForecastDTO forecastDTO);

    List<SchoolYearScoreVO> scoreLineByYear(YearScoreQueryDTO yearScoreDTO);

    void addSchoolComment(AddCommentDTO addCommentDTO);

    PageResult<CommentVO> showComment(CommentPageQueryDTO commentPageQueryDTO);

    List<SchoolSceneryVO> getSchool1(ProvinceQueryDTO provinceQueryDTO);

    List<SchoolSceneryVO> getSchool2(ProvinceQueryDTO provinceQueryDTO);

    List<SchoolSceneryVO> getSchool3(ProvinceQueryDTO provinceQueryDTO);

    List<SchoolSceneryVO> getSchool4(ProvinceQueryDTO provinceQueryDTO);

    List<HotMajorVO> getHotMajor();

    List<HotMajorVO> getHotMajorProfessional();

    List<School> getHotRank();

    List<School> getBasicSchool();

}
