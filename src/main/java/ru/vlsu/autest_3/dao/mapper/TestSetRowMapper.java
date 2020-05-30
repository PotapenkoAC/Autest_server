package ru.vlsu.autest_3.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.vlsu.autest_3.dao.model.QADo;
import ru.vlsu.autest_3.dao.model.TestSetDo;

import java.sql.ResultSet;
import java.sql.SQLException;


public class TestSetRowMapper implements RowMapper<TestSetDo> {

    @Override
    public TestSetDo mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TestSetDo()
                .setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setAssignedTo(collectQAData(resultSet))
                .setStatus(resultSet.getString("status"));

    }

    private String collectQAData(ResultSet resultSet) throws SQLException {
        return resultSet.getLong("created_by")
                + "@" + resultSet.getString("surname")
                + " " + resultSet.getString("name")
                + " " + resultSet.getString("patronymic");
    }
}
