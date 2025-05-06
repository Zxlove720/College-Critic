package a311.college.service;

import a311.college.dto.major.MajorDTO;
import a311.college.dto.query.major.MajorPageQueryDTO;
import a311.college.dto.query.major.MajorSchoolPageQueryDTO;
import a311.college.dto.query.major.ProfessionalClassQueryDTO;
import a311.college.dto.query.major.SubjectCategoryQueryDTO;
import a311.college.dto.query.school.CommentDTO;
import a311.college.dto.school.AddCommentDTO;
import a311.college.entity.major.Major;
import a311.college.entity.major.ProfessionalClass;
import a311.college.entity.major.SubjectCategory;
import a311.college.entity.school.School;
import a311.college.result.PageResult;
import a311.college.vo.major.DetailMajorVO;
import a311.college.vo.school.CommentVO;

import java.util.List;

public interface MajorService {

    List<SubjectCategory> getSubjectCategory(SubjectCategoryQueryDTO subjectCategoryQueryDTO);

    List<ProfessionalClass> getProfessionalClass(ProfessionalClassQueryDTO professionalClassQueryDTO);

    PageResult<Major> majorPageQuery(MajorPageQueryDTO majorDTO);

    void cacheMajor();

    DetailMajorVO getDetailMajor(MajorDTO majorDTO);

    void addFavoriteMajor(MajorDTO majorDTO);

    void deleteFavoriteMajor(MajorDTO majorDTO);

    PageResult<School> querySchools(MajorSchoolPageQueryDTO majorSchoolPageQueryDTO);

    void addMajorComment(AddCommentDTO addCommentDTO);

    List<CommentVO> showComment(CommentDTO commentDTO);
}
