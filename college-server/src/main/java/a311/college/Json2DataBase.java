package a311.college;

import com.fasterxml.jackson.databind.ObjectMapper;

import a311.college.entity.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class Json2DataBase {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/college";
    private static final String USER = "root";
    private static final String PASS = "123456";

    public static void main(String[] args) {
        // 示例JSON字符串
        String jsonString = readJsonFile("C:\\Users\\wzb\\Desktop\\北京大学_score_data.json"); // 这里应替换为实际的JSON字符串

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            College college = objectMapper.readValue(jsonString, College.class);
            saveToDatabase(college);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readJsonFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveToDatabase(College college) {
        // 获取数据库连接
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // 遍历每个省份
            for (Province province : college.getProvinces()) {
                // 遍历每一年
                for (Years years : province.getYears()) {
                    // 遍历每个类别
                    for (Category category : years.getCategorys()) {
                        // 遍历每个批次
                        for (Batch batch : category.getBatches()) {
                            // 遍历每个分数信息
                            for (Score score : batch.getScores()) {
                                // 准备SQL语句
                                String sql = "INSERT INTO tb_college (school, province, year, category, batch, major, min_score_weici) VALUES (?, ?, ?, ?, ?, ?, ?)";
                                PreparedStatement pstmt = conn.prepareStatement(sql);

                                // 设置参数
                                pstmt.setString(1, college.getSchoolName());
                                pstmt.setString(2, province.getProvince().name());
                                pstmt.setString(3, years.getYear());
                                pstmt.setString(4, category.getCategory());
                                pstmt.setString(5, batch.getBatch());
                                pstmt.setString(6, score.getMajor());
                                pstmt.setString(7, score.getMinScore_weici());

                                // 执行插入操作
                                pstmt.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
