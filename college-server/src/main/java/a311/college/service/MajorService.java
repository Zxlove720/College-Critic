package a311.college.service;

import a311.college.dto.query.major.MajorPageQueryDTO;
import a311.college.dto.query.major.ProfessionalClassQueryDTO;
import a311.college.dto.query.major.SubjectCategoryQueryDTO;
import a311.college.entity.major.Major;
import a311.college.entity.major.ProfessionalClass;
import a311.college.entity.major.SubjectCategory;
import a311.college.result.PageResult;

import java.util.List;

public interface MajorService {

    List<SubjectCategory> getSubjectCategory(SubjectCategoryQueryDTO subjectCategoryQueryDTO);

    List<ProfessionalClass> getProfessionalClass(ProfessionalClassQueryDTO professionalClassQueryDTO);

    PageResult<Major> majorPageQuery(MajorPageQueryDTO majorDTO);

    List<Major> searchMajorByName(String majorName);

}
