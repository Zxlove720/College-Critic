package a311.college.service;

import a311.college.vo.MajorVO;

import java.util.List;

public interface MajorService {

    List<MajorVO> getByLevel(int id);
}
