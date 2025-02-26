package a311.college.mapper;


import a311.college.dao.CollegePageQueryDTO;
import a311.college.entity.college.School;
import a311.college.result.PageResult;
import a311.college.vo.CollegeVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * 大学相关Mapper
 */
@Mapper
public interface CollegeMapper {


    Page<CollegeVO> pageQuery(CollegePageQueryDTO collegePageQueryDTO);

}
