package a311.college.database;

import a311.college.constant.resource.DataBaseConnectionConstant;
import a311.college.constant.resource.LogoURLConstant;
import a311.college.constant.resource.SchoolDataFilePath;
import a311.college.entity.school.*;
import a311.college.entity.temp.TempSchoolRankInfo;
import a311.college.entity.temp.TempSchoolID;
import a311.college.entity.temp.TempSchoolInfo;
import a311.college.enumeration.ProvinceEnum;
import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 大学json数据保存数据库
 */
public class College2DataBase {

    public static void main(String[] args) {
        saveSchoolData2DB();
    }

    private static void saveSchoolData2DB() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // 序列化id.json
            Map<String, TempSchoolID> map = mapper.readValue(new File(SchoolDataFilePath.ID_DATA_PATH),
                    new TypeReference<>() {
                    });
            // 获取学校名和其对应的id
            Collection<TempSchoolID> values = map.values();
            // 序列化rank.json
            List<TempSchoolRankInfo> rankData = mapper.readValue(new File(SchoolDataFilePath.RANKING_DATA_PATH),
                    mapper.getTypeFactory().constructCollectionType(List.class, TempSchoolRankInfo.class));
            // 序列化school.json
            File dir = new File(SchoolDataFilePath.COLLEGE_DATA_PATH);
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        // 先序列化为临时的TempSchoolInfo对象，然后再结合三个json文件，拼接为完整的School对象
                        TempSchoolInfo tempSchoolData = mapper.readValue
                                (file, TempSchoolInfo.class);
                        School school = new School();
                        BeanUtil.copyProperties(tempSchoolData, school);
                        // 设置学校地址和学校等级
                        int rankScore = 0;
                        for (TempSchoolRankInfo tempSchoolRankInfo : rankData) {
                            if (school.getSchoolName().equals(tempSchoolRankInfo.getSchoolName())) {
                                school.setSchoolAddr(tempSchoolRankInfo.getSchoolAddr());
                                school.setProvinceAddress(ProvinceEnum.getProvince(extractProvince(tempSchoolRankInfo.getSchoolAddr())));
                                // 特殊处理其rankList
                                String rankListStr = tempSchoolRankInfo.getRankList().toString();
                                String tempResult = rankListStr.replace("[", "");
                                String tempResult2 = tempResult.replace("]", "");
                                String result = tempResult2.replaceAll("，", ",");
                                String rankList = result.replaceAll(" ", "");
                                school.setRankList(rankList);
                                for (String s : rankList.split(",")) {
                                    switch (s) {
                                        case "本科", "双一流", "强基计划" -> rankScore += 15;
                                        case "公办", "军事类" -> rankScore += 10;
                                        case "985" -> rankScore += 30;
                                        case "211" -> rankScore += 20;
                                        case "医药类" -> rankScore += 5;
                                        case "双高计划" -> rankScore += 3;
                                    }
                                }
                                if (school.getSchoolName().contains("大学")) {
                                    rankScore += 5;
                                }
                                school.setScore((7 * rankScore + 3 * school.getProvinceAddress().getScore()) / 10);
                            }
                        }
                        // 获取学校id和头像
                        for (TempSchoolID value : values) {
                            if (school.getSchoolName().equals(value.getName())) {
                                school.setSchoolId(value.getSchoolId());
                                school.setSchoolHead(LogoURLConstant.LOGO_PREFIX + school.getSchoolId() + ".jpg");
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void saveToDatabase(School school, Connection conn) throws SQLException {
        // 插入学校
        String schoolId = school.getSchoolId();
        String insertSchool = "INSERT INTO tb_school " +
                "(school_id, school_head,school_name, school_province, school_address, rank_list, score)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(insertSchool, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, school.getSchoolId());
            statement.setString(2, school.getSchoolHead());
            statement.setString(3, school.getSchoolName());
            statement.setString(4, school.getProvinceAddress().getName());
            statement.setString(5, school.getSchoolAddr());
            statement.setString(6, updateRankList(school.getRankList()));
            statement.setInt(7, school.getScore());
            statement.executeUpdate();
        }

        // 遍历省份
        for (Province province : school.getProvinces()) {
            int provinceId;
            String insertProvince = "INSERT INTO tb_province (school_id, province_name) VALUES (?, ?)";
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
                    String insertCategory = "INSERT INTO tb_category (year_id, category_name) VALUES (?, ?)";
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
                        String insertBatch = "INSERT INTO tb_batch (category_id, batch_name) VALUES (?, ?)";
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
                        String insertScore = "INSERT INTO tb_score (batch_id, major_name, first_choice, other_choice" +
                                ", special, min_score, min_ranking) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement statement = conn.prepareStatement(insertScore)) {
                            for (Score score : batch.getScores()) {
                                statement.setInt(1, batchId);
                                String[] major = score.getMajor().split("\n");
                                statement.setString(2, major[0]);
                                String value3 = null;
                                String value4 = null;
                                String value5 = null;
                                if (major.length == 3) {
                                    value5 = major[1];
                                    String[] split = major[2].split("：")[1].split("，");
                                    if (split[0].contains("物理")) {
                                        value3 = "物理";
                                        if (split.length == 2) {
                                            value4 = split[1];
                                        }
                                    } else if (split[0].contains("历史")) {
                                        value3 = "历史";
                                        if (split.length == 2) {
                                            value4 = split[1];
                                        }
                                    } else {
                                        value3 = "不限";
                                    }
                                } else if (major.length == 2) {
                                    if (major[1].contains("选科")) {
                                        String[] split = major[1].split("：")[1].split("，");
                                        if (split[0].contains("物理")) {
                                            value3 = "物理";
                                            if (split.length == 2) {
                                                value4 = split[1];
                                            }
                                        } else if (split[0].contains("历史")) {
                                            value3 = "历史";
                                            if (split.length == 2) {
                                                value4 = split[1];
                                            }
                                        } else {
                                            value3 = "不限";
                                        }
                                    } else {
                                        value5 = major[1];
                                    }
                                }
                                if (value3 == null) {
                                    statement.setNull(3, Types.VARCHAR);
                                }
                                if (value4 == null) {
                                    statement.setNull(4, Types.VARCHAR);
                                }
                                if (value5 == null) {
                                    statement.setNull(5, Types.VARCHAR);
                                }
                                statement.setString(3, value3);
                                statement.setString(4, value4);
                                statement.setString(5, value5);
                                int[] temp = getScoreAndRanking(score.getMinScore_weici());
                                statement.setInt(6, temp[0]);
                                statement.setInt(7, temp[1]);
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

    private static final List<String> PROVINCES = Arrays.asList(
            "内蒙古", "黑龙江",
            "北京", "天津", "上海", "重庆",
            "广西", "西藏", "宁夏", "新疆",
            "香港", "澳门",
            "河北", "山西", "辽宁", "吉林", "江苏",
            "浙江", "安徽", "福建", "江西", "山东",
            "河南", "湖北", "湖南", "广东", "海南",
            "四川", "贵州", "云南", "陕西", "甘肃",
            "青海", "台湾"
    );

    public static String extractProvince(String input) {
        for (String province : PROVINCES) {
            if (input.startsWith(province)) {
                return province;
            }
        }
        return "";
    }

    private static String updateRankList(String rankList) {
        String tempResult = rankList.replace("[", "");
        String tempResult2 = tempResult.replace("]", "");
        String result = tempResult2.replaceAll("，", ",");
        result = result.replaceAll(" ", "");
        return result;
    }
}