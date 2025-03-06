package a311.college.mapper.major;

import a311.college.dto.major.MajorDTO;
import a311.college.result.Result;
import a311.college.vo.MajorVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MajorMapper {

    List<MajorVO> selectMajors(MajorDTO majorDTO);

    Result<List<MajorVO>> selectByName(String majorName);
}
