package a311.college.database.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "校园风光")
public class Image implements Serializable {

    @Schema(description = "存储连接")
    private String url;
}
