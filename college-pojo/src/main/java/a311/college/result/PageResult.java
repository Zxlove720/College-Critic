package a311.college.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询统一返回结果
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {

    // 分页查询总记录数
    private Long total;

    // 当前页数封装的记录集合
    private List<T> records;

}


