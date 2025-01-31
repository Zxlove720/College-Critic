package a311.college.mapper;

import a311.college.dao.UserPageQueryDTO;
import a311.college.entity.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户相关Mapper
 */
@Mapper
//TODO建表之后根据subjects列的具体情况可能会有所修改
public interface UserMapper {

    /**
     * 用户登录 传统
     * @param username 用户名
     * @return User
     */
    @Select("select * from user where username = #{username}")
    User getUserByUsername(String username);

    /**
     * 用户分页查询
     * @param userPageQueryDTO 用户分页查询DTO
     * @return Page<User>
     */
    Page<User> pageQuery(UserPageQueryDTO userPageQueryDTO);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @Select("select * from user where id = #{id}")
    User selectById(Long id);

    /**
     * 根据id删除用户
     * @param id 用户id
     */
    @Delete("delete from user where id = #{id}")
    void deleteById(Long id);

    /**
     * 新增用户（用户注册）
     * @param user User实体对象
     */
    @Insert("insert into user (username, password, phone, email, head, year, subjects, province, grade, rank, status, " +
            "area, createTime, updateTime) values (#{username}, #{password}, #{phone}, #{email}, #{head}, #{year}, " +
            "#{subjects}, #{province}, #{grade}, #{rank}, #{status}, #{area}, #{createTime}, #{updateTime})")
    void insert(User user);
}
