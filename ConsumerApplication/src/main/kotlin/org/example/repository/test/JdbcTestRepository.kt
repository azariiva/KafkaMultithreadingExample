package org.example.repository.test

import org.example.model.TestEntity
import org.joda.time.DateTime
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.util.*

@Repository
class JdbcTestRepository(
    private val jdbc: NamedParameterJdbcTemplate
) : TestRepository {
    @Transactional
    override fun createOnDuplicateIgnoreReturningId(testEntity: TestEntity): UUID {
        Thread.sleep(10_000)
        return jdbc.query("""
            INSERT INTO test_table 
            VALUES (:id, :value, :created) 
                ON CONFLICT (value) 
                    DO UPDATE SET value=EXCLUDED.value RETURNING (id)
        """.trimIndent(),
            MapSqlParameterSource()
                .addValue("id", UUID.randomUUID())
                .addValue("value", testEntity.value)
                .addValue("created", Timestamp(DateTime.now().millis)),
            ResultSetExtractor { rs ->
                if (rs.next()) {
                    rs.getObject("id", UUID::class.java)
                } else {
                    throw NoSuchElementException()
                }
            })!!
    }
}