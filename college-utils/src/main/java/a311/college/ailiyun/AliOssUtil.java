package a311.college.ailiyun;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

/**
 * 阿里云OSS工具类
 */
@Data
@Slf4j
@AllArgsConstructor
public class AliOssUtil {

    private String endpoint;
    private String bucketName;

    public String upload(byte[] bytes, String fileName) {
        String ossAccessKeyId = System.getenv("OSS_ACCESS_KEY_ID");
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint,
                    System.getenv("OSS_ACCESS_KEY_ID"),
                    System.getenv("OSS_ACCESS_KEY_SECRET"));

        try {
            // 创建Oss的PutObject请求
            ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error UserAIMessageVO:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error UserAIMessageVO:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                // 释放资源
                ossClient.shutdown();
            }
        }
        // 文件的访问路径规则：https://bucketName.endpoint.fileName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(fileName);
        log.info("文件{}成功上传到{}", fileName, stringBuilder);
        return stringBuilder.toString();
    }

}
