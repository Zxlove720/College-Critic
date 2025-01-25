package a311.college.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT令牌配置类
 */
@Component
@ConfigurationProperties(prefix = "college.jwt")
@Data
public class JWTProperties {

    private String userSecretKey;
    private Long userTime;
    private String userTokenName;

}
