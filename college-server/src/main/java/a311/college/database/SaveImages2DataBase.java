package a311.college.database;

import a311.college.ailiyun.AliOssUtil;
import a311.college.constant.resource.DataBaseConnectionConstant;
import a311.college.constant.resource.SchoolDataFilePath;
import cn.hutool.core.lang.UUID;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 将校园风光保存到数据库
 */
public class SaveImages2DataBase {

    private static final AliOssUtil aliOssUtil = new AliOssUtil("oss-cn-chengdu.aliyuncs.com", "wzb-study");

    public static void main(String[] args) throws IOException, SQLException {
        // 1.获取照片文件
        File dir = new File(SchoolDataFilePath.COLLEGE_IMAGES_PATH);
        File[] images = dir.listFiles();
        if (images != null) {
            for (File image : images) {
                // 2.建立新文件名
                // 2.1获取原始文件后缀
                String extension = image.getName().split("\\.")[1];
                // 2.2构建新文件名
                String objectName = "college:image:" + UUID.randomUUID() + "." + extension;
                // 2.3创建文件请求路径
                String filePath = aliOssUtil.upload(getFileBytes(image), objectName);
                // 3.将图片的连接加入数据库
                Connection connection = DriverManager.getConnection(DataBaseConnectionConstant.URL,
                        DataBaseConnectionConstant.USERNAME,
                        DataBaseConnectionConstant.PASSWORD);
                String sql = "Insert INTO tb_image (url) VALUE(?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, filePath);
                preparedStatement.executeUpdate();
                connection.close();

                Connection connection2 = DriverManager.getConnection("jdbc:mysql://8.137.37.221/college",
                        "root",
                        "123456");
                PreparedStatement preparedStatement2 = connection2.prepareStatement(sql);
                preparedStatement2.setString(1, filePath);
                preparedStatement2.executeUpdate();
                connection2.close();
            }
        }
    }

    private static byte[] getFileBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            int offset = 0;
            int bytesRead;
            while (offset < buffer.length &&
                    (bytesRead = fis.read(buffer, offset, buffer.length - offset)) != -1) {
                offset += bytesRead;
            }
            if (offset != buffer.length) {
                throw new IOException("文件未完全读取: " + file.getName());
            }
            return buffer;
        }
    }
}
