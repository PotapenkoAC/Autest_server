package ru.vlsu.autest_3.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.vlsu.autest_3.dao.TestDao;
import ru.vlsu.autest_3.dao.mapper.ActionRowMapper;
import ru.vlsu.autest_3.dao.mapper.TestCaseRowMapper;
import ru.vlsu.autest_3.dao.mapper.TestSetRowMapper;
import ru.vlsu.autest_3.dao.model.ActionDo;
import ru.vlsu.autest_3.dao.model.TestCaseDo;
import ru.vlsu.autest_3.dao.model.TestSetDo;

import java.util.List;
import java.util.Optional;

@Component
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
            "    INNER JOIN qa_specialist qs on test_set.qa_id = qs.id\n" +
            "    WHERE test_set.id=:setId";

    private static final String GET_ACTIONS_BY_CASES_ID_SQL = "SELECT *\n" +
            "FROM action\n" +
            "WHERE test_case_id IN (:casesId)\n" +
            "GROUP BY test_case_id, \"order\",id\n" +
            "ORDER BY \"order\" ASC";
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
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("casesId",ids);
        return jdbcTemplate.query(GET_ACTIONS_BY_CASES_ID_SQL,params, new ActionRowMapper());
    }

}
