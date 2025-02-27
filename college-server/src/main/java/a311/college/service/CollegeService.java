package a311.college.service;

import a311.college.dto.CollegePageQueryDTO;
import a311.college.result.PageResult;
import a311.college.vo.CollegeVO;

/**
 * 大学相关服务
 */
public interface CollegeService {

    PageResult<CollegeVO> pageSelect(CollegePageQueryDTO collegePageQueryDTO);
}
