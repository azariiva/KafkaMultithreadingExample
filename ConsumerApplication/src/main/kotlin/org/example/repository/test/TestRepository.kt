package org.example.repository.test

import org.example.model.TestEntity
import java.util.*

interface TestRepository {
    fun createOnDuplicateIgnoreReturningId(testEntity: TestEntity): UUID
}