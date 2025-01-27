package a311.college.mapper;

import a311.college.dao.UserPageQueryDTO;
import a311.college.entity.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户相关Mapper
 */
@Mapper
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
}
