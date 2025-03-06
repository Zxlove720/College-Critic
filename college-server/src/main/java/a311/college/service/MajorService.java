package a311.college.service;

import a311.college.dto.major.MajorDTO;
import a311.college.result.Result;
import a311.college.vo.MajorVO;

import java.util.List;

public interface MajorService {

    List<MajorVO> getMajors(MajorDTO majorDTO);

    Result<List<MajorVO>> getMajorByName(String majorName);
}
