package a311.college.config;


import a311.college.ailiyun.AliOssUtil;
import a311.college.properties.AliOSSProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS配置类，用于创建AliOssUtil对象
 *
 */
@Slf4j
@Configuration
public class OssConfiguration {


    /**
     * 创建阿里云OSS工具类
     *
     * @param aliOSSProperties 阿里云配置
     * @return AliOssUtil 阿里云OSS工具类
     */
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOSSProperties aliOSSProperties) {
        log.info("开始创建阿里云OSS工具类对象");
        return new AliOssUtil(
                aliOSSProperties.getEndpoint(),
                aliOSSProperties.getBucketName()
        );
    }
}
