package a311.college.entity.volunteer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 志愿表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "志愿表实体类")
public class VolunteerTable {

    @Schema(description = "志愿表id")
    private Integer tableId;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "志愿表名称")
    private String tableName;

    @Schema(description = "志愿个数")
    private Integer count;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
