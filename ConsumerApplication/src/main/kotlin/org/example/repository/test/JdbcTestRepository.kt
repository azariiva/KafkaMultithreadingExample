package org.example.repository.test

import org.example.model.TestEntity
import org.joda.time.DateTime
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

@Repository
class JdbcTestRepository(
    private val jdbc: NamedParameterJdbcTemplate
) : TestRepository {
    @Transactional
    override fun createOnDuplicateIgnore(testEntity: TestEntity) {
        Thread.sleep(10_000)
        jdbc.update(
            """
            INSERT INTO test_table 
            VALUES (:id, :value, :created) 
                ON CONFLICT (id) DO NOTHING 
                """.trimIndent(),
            MapSqlParameterSource()
                .addValue("id", testEntity.generateId())
                .addValue("value", testEntity.value)
                .addValue("created", Timestamp(DateTime.now().millis)),
        )
    }
}