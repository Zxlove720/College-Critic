package a311.college;

import a311.college.entity.college.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.*;

public class Json2DataBase {
private static final String DB_URL = "jdbc:mysql://localhost:3306/college";
    private static final String USER = "root";
    private static final String PASS = "123456";

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            School school = mapper.readValue(new File("C:\\Users\\wzb\\Desktop\\北京大学_score_data.json"), School.class);
            saveToDatabase(school);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveToDatabase(School school) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            conn.setAutoCommit(false);

            // 插入学校
            int schoolId;
            String insertSchool = "INSERT INTO school (name) VALUES (?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSchool, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, school.getSchoolName());
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) schoolId = rs.getInt(1);
                    else throw new SQLException("Failed to get school ID");
                }
            }

            // 遍历省份
            for (Province province : school.getProvinces()) {
                int provinceId;
                String insertProvince = "INSERT INTO province (school_id, province) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertProvince, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, schoolId);
                    stmt.setString(2, province.getProvince().getName());
                    stmt.executeUpdate();
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) provinceId = rs.getInt(1);
                        else throw new SQLException("Failed to get province ID");
                    }
                }

                // 遍历年份
                for (Years year : province.getYears()) {
                    int yearId;
                    String insertYear = "INSERT INTO year (province_id, year) VALUES (?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(insertYear, Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setInt(1, provinceId);
                        stmt.setString(2, year.getYear());
                        stmt.executeUpdate();
                        try (ResultSet rs = stmt.getGeneratedKeys()) {
                            if (rs.next()) yearId = rs.getInt(1);
                            else throw new SQLException("Failed to get year ID");
                        }
                    }

                    // 遍历类别
                    for (Category category : year.getCategorys()) {
                        int categoryId;
                        String insertCategory = "INSERT INTO category (year_id, category) VALUES (?, ?)";
                        try (PreparedStatement stmt = conn.prepareStatement(insertCategory, Statement.RETURN_GENERATED_KEYS)) {
                            stmt.setInt(1, yearId);
                            stmt.setString(2, category.getCategory());
                            stmt.executeUpdate();
                            try (ResultSet rs = stmt.getGeneratedKeys()) {
                                if (rs.next()) categoryId = rs.getInt(1);
                                else throw new SQLException("Failed to get category ID");
                            }
                        }

                        // 遍历批次
                        for (Batch batch : category.getBatches()) {
                            int batchId;
                            String insertBatch = "INSERT INTO batch (category_id, batch) VALUES (?, ?)";
                            try (PreparedStatement stmt = conn.prepareStatement(insertBatch, Statement.RETURN_GENERATED_KEYS)) {
                                stmt.setInt(1, categoryId);
                                stmt.setString(2, batch.getBatch());
                                stmt.executeUpdate();
                                try (ResultSet rs = stmt.getGeneratedKeys()) {
                                    if (rs.next()) batchId = rs.getInt(1);
                                    else throw new SQLException("Failed to get batch ID");
                                }
                            }

                            // 插入分数
                            String insertScore = "INSERT INTO score (batch_id, major, min_score) VALUES (?, ?, ?)";
                            try (PreparedStatement stmt = conn.prepareStatement(insertScore)) {
                                for (Score score : batch.getScores()) {
                                    stmt.setInt(1, batchId);
                                    stmt.setString(2, score.getMajor());
                                    stmt.setString(3, score.getMinScore_weici());
                                    stmt.addBatch();
                                }
                                stmt.executeBatch();
                            }
                        }
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}