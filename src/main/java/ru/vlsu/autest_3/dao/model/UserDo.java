package ru.vlsu.autest_3.dao.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDo {
    private Long id;
    private String login;
    private String password;
    private String role;
}
