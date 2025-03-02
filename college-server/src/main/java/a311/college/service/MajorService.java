package a311.college.service;

import a311.college.vo.MajorVO;
import a311.college.vo.SubjectCategoryVO;

import java.util.List;

public interface MajorService {

    List<MajorVO> getByLevel(int id);

    List<SubjectCategoryVO> getSubjectCategory(int id);
}
