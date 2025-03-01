package a311.college.service.impl;

import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.mapper.college.CollegeMapper;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.service.CollegeService;
import a311.college.vo.CollegeSimpleVO;
import a311.college.vo.CollegeVO;
import a311.college.vo.YearScoreVO;
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
    public PageResult<CollegeSimpleVO> pageSelect(CollegePageQueryDTO collegePageQueryDTO) {
        PageHelper.startPage(collegePageQueryDTO.getPage(), collegePageQueryDTO.getPageSize());
        Page<CollegeSimpleVO> pageResult = collegeMapper.pageQuery(collegePageQueryDTO);
        // 获取总记录数
        long total = pageResult.getTotal();
        // 获取总记录
        List<CollegeSimpleVO> result = pageResult.getResult();
        return new PageResult<>(total, result);
    }

    @Override
    public CollegeVO getSchoolByName(String schoolName) {
        CollegeVO collegeVO = collegeMapper.selectByName(schoolName);
        System.out.println(collegeVO);
        return collegeVO;
    }

    @Override
    public List<CollegeVO> getByGrade(int grade, String province) {
        return collegeMapper.selectByGrade(grade, province);
    }

    @Override
    public List<CollegeVO> getByAddress(String province) {
        return collegeMapper.selectByAddress(province);
    }

    @Override
    public Result<List<YearScoreVO>> getScoreByYear(int id, String province, String year) {
        List<YearScoreVO> yearScoreVOList = collegeMapper.selectScoreByYear(id, province, year);
        for (YearScoreVO yearScoreVO : yearScoreVOList) {
            yearScoreVO.setMajorName(yearScoreVO.getMajorName()
                .substring(yearScoreVO.getMajorName().indexOf("选科要求")));
        }
        return Result.success(yearScoreVOList);
    }
}
