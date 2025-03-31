package a311.college.service;

import a311.college.dto.query.major.MajorPageQueryDTO;
import a311.college.dto.query.major.SubjectCategoryQueryDTO;
import a311.college.entity.major.Major;
import a311.college.entity.major.SubjectCategory;
import a311.college.result.PageResult;

import java.util.List;

public interface MajorService {

    PageResult<Major> majorPageQuery(MajorPageQueryDTO majorDTO);

    List<Major> searchMajorByName(String majorName);

    List<SubjectCategory> getSubjectCategory(SubjectCategoryQueryDTO subjectCategoryQueryDTO);
}
