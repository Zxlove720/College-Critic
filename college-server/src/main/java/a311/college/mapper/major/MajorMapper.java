package a311.college.mapper.major;

import a311.college.dto.major.MajorDTO;
import a311.college.dto.query.major.MajorPageQueryDTO;
import a311.college.dto.query.major.SubjectCategoryQueryDTO;
import a311.college.dto.school.AddCommentDTO;
import a311.college.entity.major.Major;
import a311.college.entity.major.ProfessionalClass;
import a311.college.entity.major.SubjectCategory;
import a311.college.vo.school.CommentVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
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
    @Select("select * from tb_subject_category where academic_level_id = #{academicLevelId}")
    List<SubjectCategory> selectSubjectCategory(SubjectCategoryQueryDTO subjectCategoryQueryDTO);

    /**
     * 查询专业类别
     *
     * @param subjectCategoryId 学科门类id
     * @return List<ProfessionalClass>
     */
    @Select("select * from tb_professional_class where subject_category_id = #{subjectCategoryId}")
    List<ProfessionalClass> selectProfessionalClass(int subjectCategoryId);

    /**
     * 专业信息分页查询
     *
     * @param majorPageQueryDTO 专业分页查询DTO
     * @return List<Major>
     */
    List<Major> pageQueryMajors(MajorPageQueryDTO majorPageQueryDTO);

    /**
     * 获取某专业分类下的所有专业
     *
     * @param professionalClassId 专业分类id
     * @return List<Major>
     */
    @Select("select * from tb_major where professional_class_id = #{professionalClassId}")
    List<Major> selectAllMajor(Integer professionalClassId);

    /**
     * 根据专业名搜索专业
     *
     * @param majorName 专业名
     * @return List<Major>
     */
    List<Major> searchMajorByName(String majorName);

    /**
     * 根据用户搜索内容搜索专业（搜索提示）
     *
     * @param message 搜索内容
     * @return List<Major>
     */
    List<Major> searchMajor(String message);

    /**
     * 根据id查询专业
     *
     * @param majorId 专业id
     * @return Major 专业实体类
     */
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

    /**
     * 用户评价专业
     *
     * @param addCommentDTO 用户评价DTO
     */
    @Insert("insert into tb_comment set user_id = #{userId}, major_id = #{majorId}, major_name = #{majorName}, " +
            "comment = #{comment}, time = #{time}")
    void addComment(AddCommentDTO addCommentDTO);

    /**
     * 查询用户评价
     *
     * @param majorId 专业id
     * @return List<CommentVO>
     */
    List<CommentVO> selectComment(Integer majorId);

}
