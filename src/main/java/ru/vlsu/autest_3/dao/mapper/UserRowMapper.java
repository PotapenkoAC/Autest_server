package ru.vlsu.autest_3.dao.mapper;


import org.springframework.jdbc.core.RowMapper;
import ru.vlsu.autest_3.dao.model.UserDo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserDo> {

    @Override
    public UserDo mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserDo()
                .setId(resultSet.getLong("id"))
                .setLogin(resultSet.getString("login"))
                .setPassword(resultSet.getString("password"))
                .setRole(resultSet.getString("role"))
                .setToken(resultSet.getString("token"));
    }
}
