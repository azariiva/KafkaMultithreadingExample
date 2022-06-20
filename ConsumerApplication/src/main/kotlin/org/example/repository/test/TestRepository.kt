package org.example.repository.test

import org.example.model.TestEntity

interface TestRepository {
    fun createOnDuplicateIgnore(testEntity: TestEntity)
}