package org.jarvis.ws.medicine.handler;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created: KimChheng
 * Date: 13-Nov-2020 Fri
 * Time: 10:10 PM
 */
public class AnyEnumHandler<E extends Enum<E>> extends EnumTypeHandler<E> {

    private final Class<E> type;

    public AnyEnumHandler(Class<E> type) {
        super(type);
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, parameter.toString());
        } else {
            ps.setObject(i, parameter.toString(), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return s == null ? null : convertToEnum(s.toUpperCase());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return s == null ? null : convertToEnum(s.toUpperCase());
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return s == null ? null : convertToEnum(s.toUpperCase());
    }

    protected E convertToEnum(String value) {
        return Enum.valueOf(this.type, value);
    }
}
