package a311.college.mapper.major;

import a311.college.dto.query.major.MajorQueryDTO;
import a311.college.vo.major.BriefMajorVO;
import a311.college.vo.major.MajorVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MajorMapper {

    List<MajorVO> selectMajors(MajorQueryDTO majorDTO);

    List<MajorVO> selectByName(String majorName);

    List<BriefMajorVO> searchMajor(String message);
}
