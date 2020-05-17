package ru.vlsu.autest_3.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import ru.vlsu.autest_3.dao.TestDao;
import ru.vlsu.autest_3.dao.model.ActionDo;
import ru.vlsu.autest_3.dao.model.TestCaseDo;
import ru.vlsu.autest_3.dao.model.TestSetDo;
import ru.vlsu.autest_3.manager.TestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TestManagerImpl implements TestManager {
    private final TestDao testDao;

    @Autowired
    public TestManagerImpl(TestDao testDao) {
        this.testDao = testDao;
    }

    @Override
    public List<TestCaseDo> getTestCaseBySetId(long id) {
        return testDao.getTestCasesBySetId(id);
    }

    @Override
    public TestSetDo getTestSetById(long id) {
        Optional<TestSetDo> set = testDao.getTestSetById(id);
        List<TestCaseDo> cases = testDao.getTestCasesBySetId(id);
        return set.map(testSetDo -> testSetDo.setCases(cases)).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @Override
    public TestSetDo getFullTestSetById(long id) {
        Optional<TestSetDo> set = testDao.getTestSetById(id);
        List<TestCaseDo> cases = testDao.getTestCasesBySetId(id);
        List<ActionDo> actions = testDao.getActionsByCasesId(getCasesId(cases));
        collectActionsToCases(cases, actions);
        return set.map(testSetDo -> testSetDo.setCases(cases)).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    private void collectActionsToCases(List<TestCaseDo> cases, List<ActionDo> actions) {
        for (TestCaseDo curCase : cases) {
            List<ActionDo> tempActions = new ArrayList<>();
            for (ActionDo action : actions) {
                if (curCase.getId().equals(action.getTestCaseId())) {
                    tempActions.add(action);
                }
            }
            curCase.setActions(tempActions);
        }
    }

    private List<Long> getCasesId(List<TestCaseDo> cases) {
        List<Long> result = new ArrayList<>();
        cases.forEach(caseDo -> result.add(caseDo.getId()));
        return result;
    }

}
