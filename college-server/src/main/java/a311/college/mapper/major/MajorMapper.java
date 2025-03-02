package a311.college.mapper.major;

import a311.college.vo.MajorVO;
import a311.college.vo.SubjectCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MajorMapper {

    /**
     * 根据培养层次获取专业
     *
     * @param id 层次id
     * @return MajorVO
     */
    List<MajorVO> selectByLevelId(int id);

    /**
     * 根据培养层次获取学科门类
     *
     * @param id 培养层次id
     * @return List<SubjectCategoryVO>
     */
    @Select("select * from subject_category where academic_level_id = #{id}")
    List<SubjectCategoryVO> selectSubjectCategory(int id);
}
