package ru.vlsu.autest_3.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vlsu.autest_3.dao.UserDao;
import ru.vlsu.autest_3.dao.model.UserDo;

@RestController
public class LoginController {


    private final UserDao userDao;

    @Autowired
    public LoginController(UserDao userDao) {
        this.userDao = userDao;
    }


    @PostMapping("rest/login")
    public HttpEntity<String> login(@RequestBody UserDo user) {
        String token = userDao.login(user.getLogin(),user.getPassword());
        ResponseEntity.BodyBuilder response;
        response = token.isEmpty()? ResponseEntity.status(403) : ResponseEntity.status(201);
        return response.body(token);
    }
}
