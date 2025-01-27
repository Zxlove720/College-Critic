package a311.college.mapper;

import a311.college.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户相关Mapper
 */
@Mapper
public interface UserMapper {

    @Select("select * from college where username = #{username}")
    User getUserByUsername(String username);
}
