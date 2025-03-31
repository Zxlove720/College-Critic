package a311.college.service;

import a311.college.dto.query.major.MajorQueryDTO;
import a311.college.entity.major.Major;

import java.util.List;

public interface MajorService {

    List<Major> getMajors(MajorQueryDTO majorDTO);

    List<Major> getMajorByName(String majorName);
}
