package a311.college.result;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "分页查询统一返回结果")
public class PageResult<T> {

    @Schema(description = "分页查询总记录数")
    private Long total;

    @Schema(description = "当前页数封装的记录集合")
    private List<T> records;

}


