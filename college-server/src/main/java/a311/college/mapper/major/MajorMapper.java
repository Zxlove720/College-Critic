package a311.college.mapper.major;

import a311.college.dto.query.major.MajorPageQueryDTO;
import a311.college.dto.query.major.ProfessionalClassQueryDTO;
import a311.college.dto.query.major.SubjectCategoryQueryDTO;
import a311.college.entity.major.Major;
import a311.college.entity.major.ProfessionalClass;
import a311.college.entity.major.SubjectCategory;
import a311.college.vo.major.BriefMajorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MajorMapper {

    /**
     * 专业信息分页查询
     *
     * @param majorPageQueryDTO 专业分页查询DTO
     * @return List<Major>
     */
    List<Major> pageQueryMajors(MajorPageQueryDTO majorPageQueryDTO);

    /**
     * 根据专业名搜索专业
     *
     * @param majorName 专业名
     * @return List<Major>
     */
    List<Major> searchMajorByName(String majorName);

    /**
     * 专业搜索提示
     *
     * @param message 搜索信息
     * @return List<BriefMajorVO>
     */
    List<BriefMajorVO> searchMajor(String message);

    /**
     * 根据id查询专业
     *
     * @param majorId 专业id
     * @return Major 专业实体类
     */
    @Select("select * from tb_major where major_id = #{majorId}")
    Major selectById(Integer majorId);

    /**
     * 查询学科门类
     *
     * @param subjectCategoryQueryDTO 学科门类查询DTO
     * @return List<SubjectCategory>
     */
    @Select("select * from tb_subject_category where academic_level_id = #{academicId}")
    List<SubjectCategory> selectSubjectCategory(SubjectCategoryQueryDTO subjectCategoryQueryDTO);


    @Select("select * from tb_professional_class where subject_category_id = #{subjectCategoryId}")
    List<ProfessionalClass> selectProfessionalClass(ProfessionalClassQueryDTO professionalClassQueryDTO);
}
