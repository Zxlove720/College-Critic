package a311.college.service.impl;

import a311.college.dto.query.major.MajorQueryDTO;
import a311.college.mapper.major.MajorMapper;
import a311.college.result.Result;
import a311.college.service.MajorService;
import a311.college.vo.major.MajorVO;
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
    public List<MajorVO> getMajors(MajorQueryDTO majorDTO) {
        return majorMapper.selectMajors(majorDTO);
    }

    @Override
    public List<MajorVO> getMajorByName(String majorName) {
        return majorMapper.selectByName(majorName);
    }
}
