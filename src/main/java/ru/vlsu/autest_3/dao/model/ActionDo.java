package ru.vlsu.autest_3.dao.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;

@Data
@Accessors(chain = true)
public class ActionDo {
    private Long id;
    private Integer type; // {TOUCH, PASTE_TEXT, SCROLL}
    private Integer order;
    private String elem_id; //guid
    private String feature; // key for metadata in format feature@key@key... {NETWORK,PASTE,WAIT}
    private String status; // {TODO, DONE, IN_PROGRESS, BLOCKED, FAILED}
    private String definition;
    private String description;
    private String expectedResult;
    private byte[] attachment;
    private Long testCaseId;
    private HashMap<String, String> metadata;
}
