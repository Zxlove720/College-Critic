package a311.college.mapper.user;

import a311.college.annotation.AutoFill;
import a311.college.dto.user.UserPageQueryDTO;
import a311.college.entity.user.User;
import a311.college.enumeration.OperationType;
import a311.college.vo.UserVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * 用户相关Mapper
 */
@Mapper
public interface UserMapper {

    /**
     * 用户传统登录
     *
     * @param phone 手机号
     * @return User
     */
    @Select("select * from tb_user where phone = #{phone}")
    User selectByPhone(String phone);

    @Select("select * from tb_user where id = #{id}")
    UserVO selectById(Long id);

    @Delete("delete from tb_user where id = #{id}")
    void deleteById(Long id);

    @AutoFill(OperationType.INSERT)
    @Insert("insert into tb_user(username, password, phone, email, head, year, province, pattern, subjects," +
            " category, grade, ranking, city, favorite_table, college_table) values " +
            "(#{username}, #{password}, #{phone}, #{email}, #{head}, #{year}, #{province}, #{pattern}, #{subjects}," +
            "#{category}, #{grade}, #{ranking}, #{city}, #{favoriteTable}, #{collegeTable})")
    void register(User user);

    void update(User user);

    @Update("update tb_user set password = #{newPassword} where phone = #{phone}")
    @AutoFill(OperationType.UPDATE)
    void editPassword(String newPassword, String phone);

    @Select("select favorite_table from tb_user where id = #{id}")
    String selectFavoriteById(Long id);

    @Select("update tb_user set favorite_table = #{table} where id = #{id}")
    void addFavoriteTable(String table, long id);

    @Select(("select comment from tb_comment where user_id = #{id}"))
    List<String> selectComment(Long id);

}
