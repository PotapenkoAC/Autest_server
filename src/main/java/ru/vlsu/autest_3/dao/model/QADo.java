package ru.vlsu.autest_3.dao.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QADo {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String phone;
    private UserDo user;
}
