package ru.vlsu.autest_3.manager.impl;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import ru.vlsu.autest_3.dao.TestDao;
import ru.vlsu.autest_3.dao.UserDao;
import ru.vlsu.autest_3.dao.model.ActionDo;
import ru.vlsu.autest_3.dao.model.TestCaseDo;
import ru.vlsu.autest_3.dao.model.TestSetDo;
import ru.vlsu.autest_3.dao.model.dbconst.Defaults;
import ru.vlsu.autest_3.manager.TestManager;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TestManagerImpl implements TestManager {

    private final TestDao testDao;
    private final UserDao userDao;

    @Autowired
    public TestManagerImpl(TestDao testDao, UserDao userDao) {
        this.testDao = testDao;
        this.userDao = userDao;
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

    @Override
    public TestSetDo saveTestSet(TestSetDo testSet, Boolean withResult) {
        testSet.setStatus(Defaults.TEST_CASE_STATUS);
        TestSetDo result = testDao.saveTestSet(testSet);
        return withResult ? result : null;
    }

    @Override
    public TestCaseDo saveTestCase(TestCaseDo testCase, String token, Boolean withResult) {

        long id = userDao.getByToken(token).getId();
        testCase.setCreatedBy(id + "@");
        testCase.setLastModifiedBy(id + "@");
        testCase.setStatus(Defaults.TEST_CASE_STATUS);
        testCase.setLastModifiedDate(toTimestamp(ZonedDateTime.now()));
        testCase.setCreationDate(toTimestamp(ZonedDateTime.now()));
        TestCaseDo result = testDao.saveTestCase(testCase);
        return withResult ? result : null;
    }

    @Override
    public ActionDo saveAction(ActionDo action, Boolean withResult) {
        action.setStatus(Defaults.ACTION_STATUS);
        ActionDo result = testDao.saveAction(action);
        return withResult? result : null;
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

    private Timestamp toTimestamp(ZonedDateTime zoned) {
        String time = zoned.getYear() +
                "-" +
                zoned.getMonthValue() +
                "-" +
                zoned.getDayOfMonth() +
                " " +
                zoned.getHour() +
                ":" +
                zoned.getMinute() +
                ":" +
                zoned.getSecond() +
                "." +
                String.valueOf(zoned.getNano()).substring(0, 3);
        return Timestamp.valueOf(time);
    }

}
