package ru.vlsu.autest_3.dao.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.List;

@Data
@Accessors(chain = true)
public class TestCaseDo {
    private Long id;
    private String title;
    private List<ActionDo> actions;
    private Integer order;
    private Timestamp creationDate;
    private Timestamp lastModifiedDate;
    private String lastModifiedBy; // in format id@fio
    private String createdBy; // in format id@fio
    private String status; //{TODO, DONE, IN_PROGRESS, BLOCKED, FAILED}
    private Boolean isManual;
}
