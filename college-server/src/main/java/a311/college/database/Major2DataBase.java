package a311.college.database;

import a311.college.constant.resource.DataBaseConnectionConstant;
import a311.college.constant.resource.SchoolDataFilePath;
import a311.college.database.pojo.major.*;
import com.fasterxml.jackson.core.JsonpCharacterEscapes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Map;

/**
 * 将专业数据保存到数据库
 */
public class Major2DataBase {

    public static void main(String[] args) {
        try {
            // 从JSON文件反序列化数据
            EducationHierarchy data = loadDataFromFile();
            // 将数据保存到数据库
            storeToDatabase(data);
        } catch (IOException e) {
            System.err.println("文件处理异常: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("其他异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 从文件序列化数据
    private static EducationHierarchy loadDataFromFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // 处理中文key必须配置的编码
        mapper.getFactory().setCharacterEscapes(new UnicodeEscapes());
        return mapper.readValue(new File(SchoolDataFilePath.MAJOR_DATA_PATH), EducationHierarchy.class);
    }

    // 将序列化对象保存到数据库
    public static void storeToDatabase(EducationHierarchy data) {
        try (Connection conn = DriverManager.getConnection(DataBaseConnectionConstant.URL,
                DataBaseConnectionConstant.USERNAME, DataBaseConnectionConstant.PASSWORD)) {
            conn.setAutoCommit(false); // 开启事务

            // 遍历学历层次
            for (Map.Entry<String, AcademicLevel> levelEntry : data.getAcademicLevelMap().entrySet()) {
                int levelId = insertAcademicLevel(conn, levelEntry.getKey());

                // 遍历学科门类
                for (Map.Entry<String, SubjectCategory> subjectEntry : levelEntry.getValue().getSubjectCategoryMap().entrySet()) {
                    int subjectId = insertSubjectCategory(conn, subjectEntry.getKey(), levelId);

                    // 遍历专业类
                    for (Map.Entry<String, ProfessionalClass> classEntry : subjectEntry.getValue().getProfessionalClassMap().entrySet()) {
                        int classId = insertProfessionalClass(conn, classEntry.getKey(), subjectId);

                        // 插入专业详情
                        for (Major major : classEntry.getValue().getMajorList()) {
                            insertMajor(conn, major, classId);
                        }
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            System.err.println("数据库操作异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int insertAcademicLevel(Connection conn, String name) throws SQLException {
        String sql = "INSERT INTO tb_academic_level (name) VALUES (?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    private static int insertSubjectCategory(Connection conn, String name, int levelId) throws SQLException {
        String sql = "INSERT INTO tb_subject_category (academic_level_id, name) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, levelId);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    private static int insertProfessionalClass(Connection conn, String name, int subjectId) throws SQLException {
        String sql = "INSERT INTO tb_professional_class (subject_category_id, name) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, subjectId);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    private static void insertMajor(Connection conn, Major major, int classId) throws SQLException {
        String sql = "INSERT INTO tb_major (class_id, major_name, major_code, major_year, degrees, gender, avg_salary) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, classId);
            preparedStatement.setString(2, major.getMajorName());
            preparedStatement.setString(3, major.getMajorCode());
            preparedStatement.setString(4, major.getMajorYear());
            preparedStatement.setString(5, String.join(",", major.getMajorDegree()));
            preparedStatement.setString(6, major.getGender());
            preparedStatement.setInt(7, major.getAverageSalary());
            preparedStatement.executeUpdate();
        }
    }

}

// 辅助类：解决JSON中文字段序列化问题
class UnicodeEscapes extends JsonpCharacterEscapes {
    @Override
    public int[] getEscapeCodesForAscii() {
        int[] esc = CharacterEscapes.standardAsciiEscapesForJSON();
        // 不转义中文字符
        for (int i = 0; i < 256; i++) {
            if (i > 0x7F) {
                esc[i] = CharacterEscapes.ESCAPE_NONE;
            }
        }
        return esc;
    }
}
