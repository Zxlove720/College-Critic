package a311.college.service.impl;

import a311.college.dto.major.MajorDTO;
import a311.college.mapper.major.MajorMapper;
import a311.college.service.MajorService;
import a311.college.vo.MajorVO;
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
    public List<MajorVO> getMajors(MajorDTO majorDTO) {
        return majorMapper.selectMajors(majorDTO);
    }
}
