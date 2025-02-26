package a311.college.TypeHandler;

import a311.college.enumeration.subjectEnum.SubjectsEnum;
import cn.hutool.json.JSONUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 用户序列化/反序列化工具
 */
@Component
public class UserHandler extends BaseTypeHandler<List<SubjectsEnum>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<SubjectsEnum> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSONUtil.toJsonStr(parameter));
    }

    @Override
    public List<SubjectsEnum> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return JSONUtil.toList(json, SubjectsEnum.class);
    }

    @Override
    public List<SubjectsEnum> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return JSONUtil.toList(json, SubjectsEnum.class);
    }

    @Override
    public List<SubjectsEnum> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return JSONUtil.toList(json, SubjectsEnum.class);
    }
}
