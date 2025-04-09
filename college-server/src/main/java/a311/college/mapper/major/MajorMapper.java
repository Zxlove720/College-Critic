package a311.college.mapper.major;

import a311.college.dto.major.MajorDTO;
import a311.college.dto.query.major.MajorPageQueryDTO;
import a311.college.dto.query.major.ProfessionalClassQueryDTO;
import a311.college.dto.query.major.SubjectCategoryQueryDTO;
import a311.college.entity.major.Major;
import a311.college.entity.major.ProfessionalClass;
import a311.college.entity.major.SubjectCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MajorMapper {

    /**
     * 查询学科门类
     *
     * @param subjectCategoryQueryDTO 学科门类查询DTO
     * @return List<SubjectCategory>
     */
    @Select("select * from tb_subject_category where academic_level_id = #{academicId}")
    List<SubjectCategory> selectSubjectCategory(SubjectCategoryQueryDTO subjectCategoryQueryDTO);

    /**
     * 查询专业类别
     *
     * @param professionalClassQueryDTO 专业类别查询DTO
     * @return List<ProfessionalClass>
     */
    @Select("select * from tb_professional_class where subject_category_id = #{subjectCategoryId}")
    List<ProfessionalClass> selectProfessionalClass(ProfessionalClassQueryDTO professionalClassQueryDTO);

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
    List<Major> searchMajor(String message);

    /**
     * 根据id查询专业
     *
     * @param majorId 专业id
     * @return Major 专业实体类
     */
    @Select("select * from tb_major where major_id = #{majorId}")
    Major selectById(Integer majorId);

    /**
     * 判断该专业是否已经被收藏
     *
     * @param majorDTO 专业DTO
     * @return int 收藏表中符合条件专业数量
     */
    @Select("select count(major_id) from tb_fav_major where user_id = #{userId} and major_id = #{majorId}")
    int checkMajorDistinct(MajorDTO majorDTO);

    /**
     * 添加用户专业收藏
     *
     * @param majorDTO 专业DTO
     */
    @Select("insert into tb_fav_major (user_id, major_id) VALUES (#{userId}, #{majorId})")
    void addFavoriteMajor(MajorDTO majorDTO);

    /**
     * 删除用户收藏
     *
     * @param majorDTO 专业DTO
     */
    @Delete("delete from tb_fav_major where user_id = #{userId} and major_id = #{majorId}")
    void deleteFavoriteMajor(MajorDTO majorDTO);

}
