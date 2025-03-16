package a311.college.mapper.resource;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ResourceMapper {

    @Select("select * from tb_image")
    List<String> getAllImages();
}
