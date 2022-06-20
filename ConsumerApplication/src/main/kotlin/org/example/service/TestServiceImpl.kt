package org.example.service

import org.example.model.TestEntity
import org.example.repository.test.TestRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TestServiceImpl(
    private val repo: TestRepository
) : TestService {
    private val logger = LoggerFactory.getLogger(TestServiceImpl::class.java)

    override fun saveEntity(entity: TestEntity) {
        logger.info("Save entity {}", entity.id)
        repo.createOnDuplicateIgnore(entity)
    }
}