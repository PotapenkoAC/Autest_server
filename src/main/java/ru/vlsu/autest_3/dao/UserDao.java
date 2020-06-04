package ru.vlsu.autest_3.dao;

import org.springframework.security.core.userdetails.User;
import ru.vlsu.autest_3.dao.model.UserDo;

import java.util.Optional;

public interface UserDao {

    String login(String username, String password);

    Optional<User> findByToken(String token);

    UserDo getByToken(String token);
}
