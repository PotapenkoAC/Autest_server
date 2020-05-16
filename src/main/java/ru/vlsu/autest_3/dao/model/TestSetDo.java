package ru.vlsu.autest_3.dao.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TestSetDo {
    private List<TestCaseDo> cases;
    private Long id;
    private String title;
    private String assignedTo; //in format id@fio
    private String status; // {OPENED, ASSIGNED, IN_PROGRESS, POSTPONED, DONE, CLOSED}
}
