package a311.college.service.impl;

import a311.college.mapper.major.MajorMapper;
import a311.college.service.MajorService;
import a311.college.vo.MajorVO;
import a311.college.vo.SubjectCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<MajorVO> getByLevel(int id) {
        return majorMapper.selectByLevelId(id);
    }

    /**
     * 根据培养层次获取学科门类
     * @param id 培养层次id
     * @return List<SubjectCategoryVO>
     */
    @Override
    public List<SubjectCategoryVO> getSubjectCategory(int id) {
        return majorMapper.selectSubjectCategory(id);
    }
}
