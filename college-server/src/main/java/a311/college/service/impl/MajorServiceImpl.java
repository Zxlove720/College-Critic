package a311.college.service.impl;

import a311.college.mapper.major.MajorMapper;
import a311.college.service.MajorService;
import a311.college.vo.MajorVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MajorServiceImpl implements MajorService {

    private final MajorMapper majorMapper;

    @Autowired
    public MajorServiceImpl (MajorMapper majorMapper) {
        this.majorMapper = majorMapper;
    }

    /**
     * 根据培养层次获取专业
     * @param id 层次id
     */
    @Override
    public MajorVO getByLevel(int id) {
        return majorMapper.selectByLevelId(id);
    }
}
