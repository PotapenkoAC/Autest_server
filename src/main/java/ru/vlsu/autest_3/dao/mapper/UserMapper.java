package ru.vlsu.autest_3.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {


    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        return new User(
                resultSet.getString("login"),
                resultSet.getString("password"),
                true,
                true,
                true,
                true,
                AuthorityUtils.createAuthorityList(resultSet.getString("role"))
        );
    }
}
