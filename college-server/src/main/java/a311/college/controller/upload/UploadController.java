package a311.college.controller.upload;

import a311.college.ailiyun.AliOssUtil;
import a311.college.constant.API.APIConstant;
import a311.college.constant.error.FileUploadErrorConstant;
import a311.college.result.Result;
import cn.hutool.core.lang.UUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/upload")
@Tag(name = APIConstant.UPLOAD_SERVICE)
public class UploadController {

    private final AliOssUtil aliOssUtil;

    @Autowired
    public UploadController(AliOssUtil aliOssUtil) {
        this.aliOssUtil = aliOssUtil;
    }

    /**
     * 文件上传
     *
     * @param file 要上传的文件
     * @return filePath 文件路径
     */
    @PostMapping
    @Operation(summary = "文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传{}", file);
        try {
            // 获取原始文件名
            String originalFileName = file.getOriginalFilename();
            // 获取原始文件名后缀
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            // 构建新文件名
            String objectName = UUID.randomUUID().toString() + extension;
            // 创建文件请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.info("文件上传失败{}",file);
        }
        return Result.error(FileUploadErrorConstant.FILE_UPLOAD_ERROR);
    }

}
