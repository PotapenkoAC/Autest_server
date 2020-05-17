package ru.vlsu.autest_3.dao.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.RowMapper;
import ru.vlsu.autest_3.dao.model.ActionDo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ActionRowMapper implements RowMapper<ActionDo> {

    @Override
    public ActionDo mapRow(ResultSet resultSet, int i) throws SQLException {

        try {
            return new ActionDo()
                    .setId(resultSet.getLong("id"))
                    .setDefinition(resultSet.getString("definition"))
                    .setDescription(resultSet.getString("description"))
                    .setExpectedResult(resultSet.getString("expected_result"))
                    .setElem_id(resultSet.getString("elem_id"))
                    .setStatus(resultSet.getString("status"))
                    .setFeature(resultSet.getString("feature"))
                    .setOrder(resultSet.getInt("order"))
                    .setMetadata(new ObjectMapper().readValue(resultSet.getString("metadata"), HashMap.class))
                    .setTestCaseId(resultSet.getLong("test_case_id"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
