package a311.college.mapper.major;

import a311.college.vo.MajorVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MajorMapper {

    /**
     * 根据培养层次获取专业
     * @param id 层次id
     * @return MajorVO
     */
    MajorVO selectByLevelId(int id);
}
