package ru.vlsu.autest_3.dao.impl;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vlsu.autest_3.dao.TestDao;
import ru.vlsu.autest_3.dao.mapper.ActionRowMapper;
import ru.vlsu.autest_3.dao.mapper.Helper;
import ru.vlsu.autest_3.dao.mapper.TestCaseRowMapper;
import ru.vlsu.autest_3.dao.mapper.TestSetRowMapper;
import ru.vlsu.autest_3.dao.model.ActionDo;
import ru.vlsu.autest_3.dao.model.TestCaseDo;
import ru.vlsu.autest_3.dao.model.TestSetDo;
import ru.vlsu.autest_3.dao.model.dbconst.Sequences;

import java.sql.SQLType;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class TestDaoImpl implements TestDao {

    private static final String GET_TEST_CASES_BY_TEST_SET_ID_SQL =
            "SELECT tc.id               as id,\n" +
                    "       tc.title            as title,\n" +
                    "       tc.last_modified    as last_modified,\n" +
                    "       tc.status           as status,\n" +
                    "       tc.is_manual        as is_manual,\n" +
                    "       tc.creation_date    as creation_date,\n" +
                    "       sc.\"order\"        as \"order\",\n" +
                    "       created.id          as created_id,\n" +
                    "       created.name        as created_name,\n" +
                    "       created.surname     as created_surname,\n" +
                    "       created.patronymic  as created_patronymic,\n" +
                    "       modified.id         as modified_id,\n" +
                    "       modified.name       as modified_name,\n" +
                    "       modified.surname    as modified_surname,\n" +
                    "       modified.patronymic as modified_patronymic\n" +
                    "FROM test_case tc\n" +
                    "         INNER JOIN set_case sc\n" +
                    "                    on tc.id = sc.case_id\n" +
                    "         LEFT JOIN qa_specialist created on tc.created_by = created.id\n" +
                    "         LEFT JOIN qa_specialist modified on tc.last_modified_by = modified.id\n" +
                    "WHERE sc.set_id = :setId\n" +
                    "ORDER BY sc.order";

    private static final String GET_TEST_SET_BY_ID_SQL = "SELECT * FROM test_set\n" +
            "    INNER JOIN qa_specialist qs on test_set.created_by = qs.id\n" +
            "    WHERE test_set.id=:setId";

    private static final String GET_ACTIONS_BY_CASES_ID_SQL = "SELECT *\n" +
            "FROM action\n" +
            "WHERE test_case_id IN (:casesId)\n" +
            "GROUP BY test_case_id, \"order\",id\n" +
            "ORDER BY \"order\" ASC";

    private static final String GET_TEST_CASE_BY_ID_SQL =
            "SELECT tc.id               as id,\n" +
                    "       tc.title            as title,\n" +
                    "       tc.last_modified    as last_modified,\n" +
                    "       tc.status           as status,\n" +
                    "       tc.is_manual        as is_manual,\n" +
                    "       tc.creation_date    as creation_date,\n" +
                    "       sc.\"order\"          as \"order\",\n" +
                    "       created.id          as created_id,\n" +
                    "       created.name        as created_name,\n" +
                    "       created.surname     as created_surname,\n" +
                    "       created.patronymic  as created_patronymic,\n" +
                    "       modified.id         as modified_id,\n" +
                    "       modified.name       as modified_name,\n" +
                    "       modified.surname    as modified_surname,\n" +
                    "       modified.patronymic as modified_patronymic\n" +
                    "FROM test_case tc\n" +
                    "         INNER JOIN set_case sc on tc.id = sc.case_id\n" +
                    "         LEFT JOIN qa_specialist created on tc.created_by = created.id\n" +
                    "         LEFT JOIN qa_specialist modified on tc.last_modified_by = modified.id\n" +
                    "WHERE tc.id = :id;";

    private static final String GET_ACTION_BY_ID_SQL = "SELECT * FROM action WHERE id = :id";

    private static final String INSERT_TEST_CASE_SQL = "INSERT INTO test_case (id, title, last_modified, status, is_manual, created_by, creation_date, last_modified_by) " +
            "VALUES (:id, :title, :last_modified, :status, :is_manual, :created_by, :creation_date, :last_modified_by);";

    private static final String INSERT_SET_CASE_SQL = "INSERT INTO set_case (id, case_id, set_id, \"order\") " +
            "VALUES (:id, :case_id, :set_id, :order_);";

    private static final String INSERT_TEST_SET_SQL = "INSERT INTO test_set (id,title,created_by,status) VALUES(:id,:title,:qa_id,:status)";

    private static final String INSERT_ACTION_SQL = "INSERT INTO action (id, definition, description, expected_result, status, test_case_id, metadata, \"order\", type,\n" +
            "                    elem_id, feature)\n" +
            "VALUES (:id, :definition, :description, :expected_result, :status, :test_case_id, :metadata::JSON, :order_, :type, :elem_id,\n" +
            "        :feature);";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public TestDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TestCaseDo> getTestCasesBySetId(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("setId", id);
        return jdbcTemplate.query(GET_TEST_CASES_BY_TEST_SET_ID_SQL, params, new TestCaseRowMapper());
    }

    @Override
    public Optional<TestSetDo> getTestSetById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("setId", id);
        return jdbcTemplate.query(GET_TEST_SET_BY_ID_SQL, params, new TestSetRowMapper()).stream().findFirst();
    }

    @Override
    public List<ActionDo> getActionsByCasesId(List<Long> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("casesId", ids);
        return jdbcTemplate.query(GET_ACTIONS_BY_CASES_ID_SQL, params, new ActionRowMapper());
    }

    @Override
    public TestCaseDo getTestCaseById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.query(GET_TEST_CASE_BY_ID_SQL, params, new TestCaseRowMapper()).stream().findFirst().orElse(null);
    }

    @Override
    public ActionDo getActionById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.query(GET_ACTION_BY_ID_SQL, params, new ActionRowMapper()).stream().findFirst().orElse(null);
    }


    @Override
    public TestSetDo saveTestSet(TestSetDo testSet) {
        String query = "SELECT last_value FROM " + Sequences.TEST_SET_ID_SEQ;
        long testSetId = jdbcTemplate.queryForObject(query, new MapSqlParameterSource(), long.class) + 1;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", testSetId)
                .addValue("title", testSet.getTitle())
                .addValue("qa_id", Helper.getQAIdFromString(testSet.getAssignedTo()))
                .addValue("status", testSet.getStatus());
        jdbcTemplate.update(INSERT_TEST_SET_SQL, params);
        return this.getTestSetById(testSetId).orElse(null);
    }

    @Override
    public TestCaseDo saveTestCase(TestCaseDo testCase) {
        String caseSeqQuery = "SELECT last_value FROM " + Sequences.TEST_CASE_ID_SEQ;
        long testCaseId = jdbcTemplate.queryForObject(caseSeqQuery, new MapSqlParameterSource(), long.class) + 1;
        String setCaseSeqId = "SELECT last_value FROM " + Sequences.SET_CASE_ID_SEQ;
        long setCaseId = jdbcTemplate.queryForObject(setCaseSeqId, new MapSqlParameterSource(), long.class) + 1;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", testCaseId)
                .addValue("title", testCase.getTitle())
                .addValue("last_modified", testCase.getLastModifiedDate())
                .addValue("is_manual", testCase.getIsManual())
                .addValue("created_by", Long.parseLong(testCase.getCreatedBy().split("@")[0]))
                .addValue("creation_date", testCase.getCreationDate())
                .addValue("last_modified_by", Long.parseLong(testCase.getLastModifiedBy().split("@")[0]))
                .addValue("status", testCase.getStatus());
        jdbcTemplate.update(INSERT_TEST_CASE_SQL, params);
        params = new MapSqlParameterSource()
                .addValue("id", setCaseId)
                .addValue("case_id", testCaseId)
                .addValue("set_id", testCase.getTestSetId())
                .addValue("order_", testCase.getOrder());
        jdbcTemplate.update(INSERT_SET_CASE_SQL, params);
        return this.getTestCaseById(testCaseId);
    }

    @Override
    public ActionDo saveAction(ActionDo action) {
        String actionSeqQuery = "SELECT last_value FROM " + Sequences.ACTION_ID_SEQ;
        long actionId = jdbcTemplate.queryForObject(actionSeqQuery, new MapSqlParameterSource(), long.class) + 1;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", actionId)
                .addValue("definition", action.getDefinition())
                .addValue("description", action.getDescription())
                .addValue("expected_result", action.getExpectedResult())
                .addValue("status", action.getStatus())
                .addValue("test_case_id", action.getTestCaseId())
                .addValue("metadata", mapToJson(action.getMetadata()))
                .addValue("order_", action.getOrder())
                .addValue("type", action.getType())
                .addValue("elem_id", action.getElem_id())
                .addValue("feature", action.getFeature());
        jdbcTemplate.update(INSERT_ACTION_SQL, params);

        return null;
    }


    private String mapToJson(HashMap<String, String> map) {
        return new Gson().toJson(map);
    }
}
