package a311.college.mapper.user;

import a311.college.dto.user.UserPageQueryDTO;
import a311.college.entity.user.User;
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

    Page<User> pageQuery(UserPageQueryDTO userPageQueryDTO);

    @Select("select * from tb_user where id = #{id}")
    UserVO selectById(Long id);

    @Delete("delete from tb_user where id = #{id}")
    void deleteById(Long id);

    @Insert("insert into tb_user(username, password, phone, email, nickname, head, year, province, pattern, subjects," +
            " category, grade, ranking, status, city, favorite_table, college_table, create_time, update_time) values " +
            "(#{username}, #{password}, #{phone}, #{email}, #{nickname}, #{head}, #{year}, #{province}, #{pattern}, #{subjects}," +
            "#{category}, #{grade}, #{ranking}, #{status}, #{city}, #{favoriteTable}, #{collegeTable}, #{createTime}, #{updateTime})")
    void insert(User user);

    @Update("update tb_user set id = #{id} where phone = #{phone}")
    void update(User user);

    @Insert("insert into tb_user(phone, nickname) values (#{phone}, #{nickname})")
    void register(User registerUser);
}
