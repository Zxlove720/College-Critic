package a311.college.mapper.major;

import a311.college.vo.MajorVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MajorMapper {

    /**
     * 根据培养层次获取专业
     * @param id 层次id
     * @return MajorVO
     */
    List<MajorVO> selectByLevelId(int id);
}
