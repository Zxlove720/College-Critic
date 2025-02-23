package a311.college.mapper;

import a311.college.TypeHandler.UserHandler;
import a311.college.annotation.AutoFill;
import a311.college.dao.UserPageQueryDTO;
import a311.college.entity.User;
import a311.college.enumeration.OperationType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

/**
 * 用户相关Mapper
 */
//TODO建表之后根据subjects列的具体情况可能会有所修改
@Mapper
public interface UserMapper {

    /**
     * 用户登录 传统
     * @param username 用户名
     * @return User
     */
    @Results({
            @Result(property = "subjects", column = "subjects", typeHandler = UserHandler.class)
    })
    @Select("select * from tb_user where username = #{username}")
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
    @Results({
            @Result(property = "subjects", column = "subjects", typeHandler = UserHandler.class)
    })
    @Select("select * from tb_user where id = #{id}")
    User selectById(Long id);

    /**
     * 根据id删除用户
     * @param id 用户id
     */
    @Results({
            @Result(property = "subjects", column = "subjects", typeHandler = UserHandler.class)
    })
    @Delete("delete from tb_user where id = #{id}")
    void deleteById(Long id);

    /**
     * 新增用户（用户注册）
     * @param user User实体对象
     */
    @Results({
            @Result(property = "subjects", column = "subjects", typeHandler = UserHandler.class)
    })
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into tb_user (username, password, phone, email, head, year, subjects, province, grade, ranking, status, " +
            "city, create_time, update_time) values (#{username}, #{password}, #{phone}, #{email}, #{head}, #{year}, " +
            "#{subjects}, #{province}, #{grade}, #{ranking}, #{status}, #{city}, #{createTime}, #{updateTime})")
    void insert(User user);

    /**
     * 修改用户信息
     * @param user User实体对象
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(User user);
}
