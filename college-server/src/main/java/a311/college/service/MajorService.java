package a311.college.service;

import a311.college.dto.query.major.MajorQueryDTO;
import a311.college.vo.major.MajorVO;

import java.util.List;

public interface MajorService {

    List<MajorVO> getMajors(MajorQueryDTO majorDTO);

    List<MajorVO> getMajorByName(String majorName);
}
