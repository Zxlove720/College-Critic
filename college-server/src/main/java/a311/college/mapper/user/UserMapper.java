package a311.college.mapper.user;

import a311.college.annotation.AutoFill;
import a311.college.dto.major.MajorDTO;
import a311.college.dto.school.SchoolDTO;
import a311.college.entity.major.Major;
import a311.college.entity.school.School;
import a311.college.entity.user.User;
import a311.college.enumeration.OperationType;
import a311.college.vo.user.UserVO;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * 用户相关Mapper
 */
@Mapper
public interface UserMapper {

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return User实体对象
     */
    @Select("select * from tb_user where phone = #{phone}")
    User selectByPhone(String phone);

    /**
     * 根据用户id查询用户
     *
     * @param id 用户id
     * @return User实体对象
     */
    @Select("select * from tb_user where id = #{id}")
    UserVO selectById(Long id);

    /**
     * 根据id删除用户
     *
     * @param id 用户id
     */
    @Delete("delete from tb_user where id = #{id}")
    void deleteById(Long id);

    /**
     * 用户注册
     *
     * @param user User实体对象
     */
    @AutoFill(OperationType.INSERT)
    @Insert("insert into tb_user(username, password, phone, email, head, year, province, pattern, subjects," +
            " category, grade, ranking, city) values " +
            "(#{username}, #{password}, #{phone}, #{email}, #{head}, #{year}, #{province}, #{pattern}, #{subjects}," +
            "#{category}, #{grade}, #{ranking}, #{city})")
    void register(User user);

    /**
     * 更新用户信息
     *
     * @param user User实体对象
     */
    void update(User user);

    /**
     * 用户修改密码
     *
     * @param newPassword 新密码
     * @param phone       手机号
     */
    @AutoFill(OperationType.UPDATE)
    @Update("update tb_user set password = #{newPassword} where phone = #{phone}")
    void editPassword(String newPassword, String phone);

    /**
     * 查询用户收藏学校表
     *
     * @param userId 用户id
     * @return 用户收藏学校表
     */
    @Select("select * from tb_school where school_id in (select tb_fav_school.school_id from tb_fav_school where user_id = #{userId})")
    List<School> getUserFavoriteSchool(Long userId);

    /**
     * 查询用户收藏专业表
     *
     * @param userId 用户id
     * @return 用户收藏专业表
     */
    @Select("select * from tb_major where major_id in (select tb_fav_major.major_id from tb_fav_major where user_id = #{userId})")
    List<Major> getUserFavoriteMajor(Long userId);



    /**
     * 根据用户id查询用户评论
     *
     * @param id 用户id
     * @return 用户评论
     */
    @Select(("select comment from tb_comment where user_id = #{id}"))
    List<String> selectComment(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return User实体对象
     */
    @Select("select id from tb_user where username = #{username}")
    User selectByUsername(String username);


    @Delete("delete from tb_fav_school where user_id = #{userId} and school_id = #{schoolId}")
    void deleteFavoriteSchool(SchoolDTO schoolDTO);

    @Delete("delete from tb_fav_major where user_id = #{userId} and major_id = #{majorId}")
    void deleteFavoriteMajor(MajorDTO majorDTO);
}
