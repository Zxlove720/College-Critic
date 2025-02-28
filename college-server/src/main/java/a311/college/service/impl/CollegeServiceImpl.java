package a311.college.service.impl;

import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.mapper.college.CollegeMapper;
import a311.college.result.PageResult;
import a311.college.service.CollegeService;
import a311.college.vo.CollegeVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 大学相关服务实现类
 */
@Slf4j
@Service
public class CollegeServiceImpl implements CollegeService {

    private final CollegeMapper collegeMapper;

    @Autowired
    public CollegeServiceImpl(CollegeMapper collegeMapper) {
        this.collegeMapper = collegeMapper;
    }

    /**
     * 大学数据分页查询
     * @param collegePageQueryDTO 大学分页查询DTO
     * @return PageResult<CollegeVO>
     */
    @Override
    public PageResult<CollegeVO> pageSelect(CollegePageQueryDTO collegePageQueryDTO) {
        PageHelper.startPage(collegePageQueryDTO.getPage(), collegePageQueryDTO.getPageSize());
        Page<CollegeVO> pageResult = collegeMapper.pageQuery(collegePageQueryDTO);
        // 获取总记录数
        long total = pageResult.getTotal();
        // 获取总记录
        List<CollegeVO> result = pageResult.getResult();
        return new PageResult<>(total, result);
    }

    @Override
    public CollegeVO getSchoolByName(String schoolName) {
         return collegeMapper.selectByName(schoolName);
    }

    @Override
    public List<CollegeVO> getByGrade(int grade, String province) {
        return collegeMapper.selectByGrade(grade, province);
    }
}
