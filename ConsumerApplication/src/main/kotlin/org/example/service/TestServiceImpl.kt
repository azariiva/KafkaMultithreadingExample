package org.example.service

import org.example.model.TestEntity
import org.example.repository.test.TestRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class TestServiceImpl(
    private val repo: TestRepository
) : TestService {
    private val logger = LoggerFactory.getLogger(TestServiceImpl::class.java)

    override fun saveOrGetId(entity: TestEntity): UUID {
        logger.info("Received entity {}", entity.value)
        return repo.createOnDuplicateIgnoreReturningId(entity)
    }
}