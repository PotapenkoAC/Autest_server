package ru.vlsu.autest_3.dao;

import ru.vlsu.autest_3.dao.model.ActionDo;
import ru.vlsu.autest_3.dao.model.TestCaseDo;
import ru.vlsu.autest_3.dao.model.TestSetDo;

import java.util.List;
import java.util.Optional;

public interface TestCaseDao {
  //  Optional<TestSetDo> getFullTestSetById(long id);

    List<TestCaseDo> getTestCasesBySetId(long id);

    Optional<TestSetDo> getTestSetById(long id);

    List<ActionDo> getActionsByCasesId(List<Long> id);
}
