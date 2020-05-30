package ru.vlsu.autest_3.dao.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TestRunDo {
    private Long id;
    private String title;
    private Double successPercent;
    private QADo assignedTo;
}
