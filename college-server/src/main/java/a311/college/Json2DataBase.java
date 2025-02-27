package a311.college;

import a311.college.entity.college.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                            String insertScore = "INSERT INTO score (batch_id, major, min_score, min_ranking) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement stmt = conn.prepareStatement(insertScore)) {
                                for (Score score : batch.getScores()) {
                                    stmt.setInt(1, batchId);
                                    stmt.setString(2, score.getMajor());
                                    int[] temp = getScoreAndRanking(score.getMinScore_weici());
                                    stmt.setInt(3, temp[0]);
                                    stmt.setInt(4, temp[1]);
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

    private static int[] getScoreAndRanking(String input) {
        // 使用正则表达式匹配数字或可能表示缺失值的特殊字符
        Pattern pattern = Pattern.compile("\\d+|-");
        Matcher matcher = pattern.matcher(input);

        // 存储找到的所有数字或特殊字符
        int[] numbers = new int[4];
        int index = 0;

        // 查找所有符合正则表达式的数字或特殊字符
        while (matcher.find()) {
            if (index < numbers.length) {
                String match = matcher.group();
                // 如果是数字，则解析为整数
                if (!match.equals("-")) {
                    numbers[index++] = Integer.parseInt(match);
                } else {
                    // 如果是特殊字符“-”，将其视为0
                    numbers[index++] = 0;
                }
            }
        }

        // 根据索引数量判断输入格式并返回相应的结果
        if (index == 2) {
            // 第一种格式：只有两个数字
            return new int[]{numbers[0], numbers[1]};
        } else if (index >= 4) {
            // 第二种格式：返回第一个和最后一个数字
            return new int[]{numbers[0], numbers[index - 1]};
        } else {
            throw new IllegalArgumentException("输入格式不正确");
        }
    }

}