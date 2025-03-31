package a311.college.mapper.major;

import a311.college.dto.query.major.MajorQueryDTO;
import a311.college.entity.major.Major;
import a311.college.vo.major.BriefMajorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MajorMapper {

    List<Major> selectMajors(MajorQueryDTO majorDTO);

    List<Major> selectByName(String majorName);

    List<BriefMajorVO> searchMajor(String message);

    @Select("select * from major where major_id = #{majorId}")
    Major selectById(Integer majorId);
}
