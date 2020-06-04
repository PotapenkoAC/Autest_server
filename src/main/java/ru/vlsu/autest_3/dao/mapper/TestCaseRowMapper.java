package ru.vlsu.autest_3.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.vlsu.autest_3.dao.model.TestCaseDo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestCaseRowMapper implements RowMapper<TestCaseDo> {
    @Override
    public TestCaseDo mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TestCaseDo()
                .setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setOrder(resultSet.getInt("order"))
                .setCreationDate(resultSet.getTimestamp("creation_date"))
                .setCreatedBy(collectQAData(resultSet,"created_"))
                .setLastModifiedBy(collectQAData(resultSet,"modified_"))
                .setStatus(resultSet.getString("status"))
                .setIsManual((resultSet.getBoolean("is_manual")));
    }

        private String collectQAData(ResultSet resultSet,String who) throws SQLException {
            return resultSet.getLong(who+"id")
                    + "@" + resultSet.getString(who+"surname")
                    + " " + resultSet.getString(who+"name")
                    + " " + resultSet.getString(who+"patronymic");
        }
}

