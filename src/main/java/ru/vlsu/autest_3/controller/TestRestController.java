package ru.vlsu.autest_3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vlsu.autest_3.dao.model.TestCaseDo;
import ru.vlsu.autest_3.dao.model.TestSetDo;
import ru.vlsu.autest_3.manager.TestManager;

import java.util.List;

@RestController
public class TestRestController {

    @Autowired
    public TestRestController(TestManager testManager) {
        this.testManager = testManager;
    }

    private final TestManager testManager;

    @GetMapping("/test_case")
    public List<TestCaseDo> getTestCaseBySetId(@RequestParam long id) {
        return testManager.getTestCaseBySetId(id);
    }

    @GetMapping("/test_set")
    public TestSetDo getTestSetById(@RequestParam long id){
        return testManager.getTestSetById(id);
    }
}
