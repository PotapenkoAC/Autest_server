package ru.vlsu.autest_3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/full_set")
    public TestSetDo getFullTestSetById(@RequestParam long id){
        return testManager.getFullTestSetById(id);
    }

    @PostMapping("/test_set")
    public void saveTestSet(@RequestBody TestSetDo testSet){
        testManager.saveTestSet(testSet);
    }
}
