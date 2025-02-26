package a311.college.service.impl;

import a311.college.dao.CollegePageQueryDTO;
import a311.college.mapper.CollegeMapper;
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
 * 大学相关服务
 */
@Slf4j
@Service
public class CollegeServiceImpl implements CollegeService {

    private final CollegeMapper collegeMapper;

    @Autowired
    public CollegeServiceImpl(CollegeMapper collegeMapper) {
        this.collegeMapper = collegeMapper;
    }

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
}
