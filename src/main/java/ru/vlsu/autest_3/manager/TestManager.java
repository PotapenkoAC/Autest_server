package ru.vlsu.autest_3.manager;

import ru.vlsu.autest_3.dao.model.TestCaseDo;
import ru.vlsu.autest_3.dao.model.TestSetDo;

import java.util.List;

public interface TestManager {

    List<TestCaseDo> getTestCaseBySetId(long id);

    TestSetDo getTestSetById(long id);

    TestSetDo getFullTestSetById(long id);
}
