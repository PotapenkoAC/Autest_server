package ru.vlsu.autest_3.dao;

import ru.vlsu.autest_3.dao.model.ActionDo;
import ru.vlsu.autest_3.dao.model.TestCaseDo;
import ru.vlsu.autest_3.dao.model.TestSetDo;

import java.util.List;
import java.util.Optional;

public interface TestDao {
    List<TestCaseDo> getTestCasesBySetId(long id);

    Optional<TestSetDo> getTestSetById(long id);

    List<ActionDo> getActionsByCasesId(List<Long> id);

    TestCaseDo getTestCaseById(long id);

    ActionDo getActionById(long id);

    TestSetDo saveTestSet(TestSetDo testSet);

    TestCaseDo saveTestCase(TestCaseDo testCase);

    ActionDo saveAction(ActionDo action);
}
