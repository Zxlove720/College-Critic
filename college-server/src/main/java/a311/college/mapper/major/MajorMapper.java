package a311.college.mapper.major;

import a311.college.dto.major.MajorQueryDTO;
import a311.college.vo.MajorVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MajorMapper {

    List<MajorVO> selectMajors(MajorQueryDTO majorDTO);

    List<MajorVO> selectByName(String majorName);
}
