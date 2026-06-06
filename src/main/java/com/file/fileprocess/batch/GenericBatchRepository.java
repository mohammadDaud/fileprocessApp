package com.file.fileprocess.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenericBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public <T> void batchInsert(
            String sql,
            List<T> records,
            int batchSize,
            ParameterizedPreparedStatementSetter<T> setter) {

        jdbcTemplate.batchUpdate(
                sql,
                records,
                batchSize,
                setter);
    }
}