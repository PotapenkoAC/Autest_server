package ru.vlsu.autest_3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

    //GET Mappings

    @GetMapping("rest/test_case")
    public HttpEntity<List<TestCaseDo>> getTestCaseBySetId(@RequestParam long id) {
        ResponseEntity.BodyBuilder response = ResponseEntity.status(200);
        return response.body(testManager.getTestCaseBySetId(id));
    }

    @GetMapping("rest/test_set")
    public HttpEntity<TestSetDo> getTestSetById(@RequestParam long id) {
        ResponseEntity.BodyBuilder response = ResponseEntity.status(200);
        return response.body(testManager.getTestSetById(id));
    }

    @GetMapping("rest/full_set")
    public HttpEntity<TestSetDo> getFullTestSetById(@RequestParam long id) {
        System.out.println("accepted request to get full test set with id = " + id);
        ResponseEntity.BodyBuilder response = ResponseEntity.status(200);
        return response.body(testManager.getFullTestSetById(id));
    }

    //POST mappings

    @PostMapping("rest/test_set")
    public HttpEntity<TestSetDo> saveTestSet(@RequestBody TestSetDo testSet, @RequestParam(required = false, defaultValue = "false", name = "_withResult") Boolean withResult) {
        ResponseEntity.BodyBuilder response = ResponseEntity.status(201);
        return response.body(testManager.saveTestSet(testSet, withResult));
    }


    //PATCH mappings
}
