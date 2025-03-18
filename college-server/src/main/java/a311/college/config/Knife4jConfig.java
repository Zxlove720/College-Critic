package a311.college.config;


import a311.college.constant.API.APIConstant;
import a311.college.constant.API.APIPathConstant;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j配置类
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public GroupedOpenApi userApi() {
        return createRestApi(APIConstant.USER_SERVICE, APIPathConstant.USER_PATH);
    }

    @Bean
    public GroupedOpenApi collegeApi() {
        return createRestApi(APIConstant.SCHOOL_SERVICE, APIPathConstant.COLLEGE_PATH);
    }

    @Bean
    public GroupedOpenApi majorApi() {
        return createRestApi(APIConstant.MAJOR_SERVICE, APIPathConstant.MAJOR_PATH);
    }

    @Bean
    public GroupedOpenApi AIApi() {
        return createRestApi(APIConstant.DEEP_SEEK_SERVICE, APIPathConstant.DEEP_SEEK_PATH);
    }

    /**
     * 创建API
     *
     * @param groupName   分组名称
     * @param basePackage 包路径
     * @return GroupedOpenApi
     */
    private GroupedOpenApi createRestApi(String groupName, String basePackage) {
        return GroupedOpenApi.builder()
                .group(groupName) // 设置分组名称
                .packagesToScan(basePackage) // 设置扫描包的路径
                .pathsToMatch("/**") // 设置匹配所有路径
                .addOpenApiCustomizer(openApi -> openApi.info(apiInfo())) // 设置API信息
                .build();
    }

    /**
     * API简介信息
     *
     * @return Info
     */
    private Info apiInfo() {
        return new Info()
                .title("跃鲤志选服务接口") // 标题
                .description("跃鲤志选服务接口文档") // 描述
                .version("1.0.0") // 版本号
                .termsOfService("https://doc.xiaominfo.com") // 服务条款
                .contact(new Contact()
                        .name("A311")
                        .url("https://github.com/Zxlove720")
                        .email("wuzhenbo4813@qq.com")); // 联系方式
    }


}
