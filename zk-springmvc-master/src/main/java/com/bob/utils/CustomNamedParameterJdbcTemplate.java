package com.bob.utils;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CustomNamedParameterJdbcTemplate extends NamedParameterJdbcTemplate {

    private SqlStatement sqlStatement;

    public CustomNamedParameterJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    public SqlStatement getSqlStatement() {
        return sqlStatement;
    }

    public void setSqlStatement(SqlStatement sqlStatement) {
        this.sqlStatement = sqlStatement;
    }

    public List<Map<String, Object>> queryForListBySqlId(String sqlId, Map<String, Object> map) {
        String sql = sqlStatement.getValue(sqlId);
        return this.queryForList(sql, map);
    }

}
