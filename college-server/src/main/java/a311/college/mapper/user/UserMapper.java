package a311.college.mapper.user;

import a311.college.annotation.AutoFill;
import a311.college.dto.user.UserPageQueryDTO;
import a311.college.entity.user.User;
import a311.college.enumeration.OperationType;
import a311.college.vo.UserVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;


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
            " category, grade, ranking, status, city, favorite_table, college_table, create_time, update_time) values " +
            "(#{username}, #{password}, #{phone}, #{email}, #{head}, #{year}, #{province}, #{pattern}, #{subjects}," +
            "#{category}, #{grade}, #{ranking}, #{status}, #{city}, #{favoriteTable}, #{collegeTable}, #{createTime}, #{updateTime})")
    void insert(User user);

    void update(User user);

    @Insert("insert into tb_user(username, password, phone, head, year, province, pattern, subjects, grade, ranking)" +
            " values (#{username}, #{password}, #{phone}, #{head}, #{year}, #{province}, #{pattern}, #{subjects}, #{grade}, " +
            "#{ranking})")
    @AutoFill(OperationType.INSERT)
    void register(User registerUser);

    @Update("update tb_user set password = #{newPassword} where phone = #{phone}")
    @AutoFill(OperationType.UPDATE)
    void editPassword(String newPassword, String phone);

    @Select("select favorite_table from tb_user where id = #{id}")
    String selectFavoriteById(Long id);

    @Select("update tb_user set favorite_table = #{table} where id = #{id}")
    void addFavoriteTable(String table, long id);
}
