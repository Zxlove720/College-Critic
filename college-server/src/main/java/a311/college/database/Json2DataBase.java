package a311.college.database;

import a311.college.address.AddressToEnumUtil;
import a311.college.constant.DataBaseConnectionConstant;
import a311.college.constant.resource.ResourceFilePath;
import a311.college.entity.college.*;
import a311.college.entity.temp.TempSchoolID;
import a311.college.entity.temp.TempSchoolInfo;
import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 大学json数据保存数据库
 */
public class Json2DataBase {

    public static void main(String[] args) {
        saveSchoolData2DB();
    }

    private static void saveSchoolData2DB() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // 序列化id.json
            Map<String, TempSchoolID> map = mapper.readValue(new File(ResourceFilePath.ID_DATA_PATH),
                    new TypeReference<>() {
                    });
            // 获取学校名和其对应的id
            Collection<TempSchoolID> values = map.values();
            // 序列化rank.json
            List<SchoolRankInfo> rankData = mapper.readValue(new File(ResourceFilePath.RANKING_DATA_PATH),
                    mapper.getTypeFactory().constructCollectionType(List.class, SchoolRankInfo.class));
            // 序列化school.json
            File dir = new File(ResourceFilePath.DATA_PATH);
            File[] files = dir.listFiles();
            for (File file : files) {
                try {
                    // 先序列化为临时的TempSchoolInfo对象，然后再结合三个json文件，拼接为完整的School对象
                    TempSchoolInfo tempSchoolData = mapper.readValue
                            (file, TempSchoolInfo.class);
                    School school = new School();
                    BeanUtil.copyProperties(tempSchoolData, school);
                    // 设置学校地址和学校等级
                    for (SchoolRankInfo rankDatum : rankData) {
                        if (school.getSchoolName().equals(rankDatum.getSchoolName())) {
                            school.setSchoolAddr(rankDatum.getSchoolAddr());
                            school.setProvinceAddress(AddressToEnumUtil.
                                    toProvinceEnum(AddressToEnumUtil.extractProvince(school.getSchoolAddr())));
                            school.setRankList(rankDatum.getRankList());
                            break;
                        }
                    }
                    // 获取学校id和头像
                    for (TempSchoolID value : values) {
                        if (school.getSchoolName().equals(value.getName())) {
                            school.setSchoolId(value.getSchoolId());
                        }
                    }

                    try (Connection connection = DriverManager.getConnection(DataBaseConnectionConstant.URL,
                            DataBaseConnectionConstant.USERNAME, DataBaseConnectionConstant.PASSWORD)) {
                        connection.setAutoCommit(false);
                        saveToDatabase(school, connection);  // 原有招生数据存储
                        connection.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void saveToDatabase(School school, Connection conn) throws SQLException {
        // 插入学校
        String schoolId = school.getSchoolId();
        String insertSchool = "INSERT INTO tb_school (school_id, school_head,school_name, province, address, rank_list)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(insertSchool, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, school.getSchoolId());
            statement.setString(2, school.getSchoolHead());
            statement.setString(3, school.getSchoolName());
            statement.setString(4, school.getProvinceAddress().getName());
            statement.setString(4, school.getSchoolAddr());
            statement.setString(5, school.getRankList().toString());
            statement.executeUpdate();
        }

        // 遍历省份
        for (Province province : school.getProvinces()) {
            int provinceId;
            String insertProvince = "INSERT INTO tb_province (school_id, province) VALUES (?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(insertProvince, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, schoolId);
                statement.setString(2, province.getProvince().getName());
                statement.executeUpdate();
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) provinceId = rs.getInt(1);
                    else throw new SQLException("Failed to get province ID");
                }
            }

            // 遍历年份
            for (Years year : province.getYears()) {
                int yearId;
                String insertYear = "INSERT INTO tb_year (province_id, year) VALUES (?, ?)";
                try (PreparedStatement statement = conn.prepareStatement(insertYear, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1, provinceId);
                    statement.setString(2, year.getYear());
                    statement.executeUpdate();
                    try (ResultSet rs = statement.getGeneratedKeys()) {
                        if (rs.next()) yearId = rs.getInt(1);
                        else throw new SQLException("Failed to get year ID");
                    }
                }

                // 遍历类别
                for (Category category : year.getCategorys()) {
                    int categoryId;
                    String insertCategory = "INSERT INTO tb_category (year_id, category) VALUES (?, ?)";
                    try (PreparedStatement statement = conn.prepareStatement(insertCategory, Statement.RETURN_GENERATED_KEYS)) {
                        statement.setInt(1, yearId);
                        statement.setString(2, category.getCategory());
                        statement.executeUpdate();
                        try (ResultSet rs = statement.getGeneratedKeys()) {
                            if (rs.next()) categoryId = rs.getInt(1);
                            else throw new SQLException("Failed to get category ID");
                        }
                    }

                    // 遍历批次
                    for (Batch batch : category.getBatches()) {
                        int batchId;
                        String insertBatch = "INSERT INTO tb_batch (category_id, batch) VALUES (?, ?)";
                        try (PreparedStatement statement = conn.prepareStatement(insertBatch, Statement.RETURN_GENERATED_KEYS)) {
                            statement.setInt(1, categoryId);
                            statement.setString(2, batch.getBatch());
                            statement.executeUpdate();
                            try (ResultSet rs = statement.getGeneratedKeys()) {
                                if (rs.next()) batchId = rs.getInt(1);
                                else throw new SQLException("Failed to get batch ID");
                            }
                        }

                        // 插入分数
                        String insertScore = "INSERT INTO tb_score (batch_id, major, min_score, min_ranking) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement statement = conn.prepareStatement(insertScore)) {
                            for (Score score : batch.getScores()) {
                                statement.setInt(1, batchId);
                                statement.setString(2, score.getMajor());
                                int[] temp = getScoreAndRanking(score.getMinScore_weici());
                                statement.setInt(3, temp[0]);
                                statement.setInt(4, temp[1]);
                                statement.addBatch();
                            }
                            statement.executeBatch();
                        }
                    }
                }
            }
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