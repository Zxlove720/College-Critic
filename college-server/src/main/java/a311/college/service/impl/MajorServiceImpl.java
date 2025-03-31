package a311.college.service.impl;

import a311.college.dto.query.major.MajorQueryDTO;
import a311.college.entity.major.Major;
import a311.college.mapper.major.MajorMapper;
import a311.college.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {

    private final MajorMapper majorMapper;

    @Autowired
    public MajorServiceImpl(MajorMapper majorMapper) {
        this.majorMapper = majorMapper;
    }

    @Override
    public List<Major> getMajors(MajorQueryDTO majorDTO) {
        return majorMapper.selectMajors(majorDTO);
    }

    @Override
    public List<Major> getMajorByName(String majorName) {
        return majorMapper.selectByName(majorName);
    }
}
