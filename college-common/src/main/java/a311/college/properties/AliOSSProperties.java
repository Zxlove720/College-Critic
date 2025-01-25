package a311.college.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
// 该注解会从application配置文件中读取属性，但是类中属性必须和其名字一致
@ConfigurationProperties(prefix = "college.alioss")
public class AliOSSProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}
