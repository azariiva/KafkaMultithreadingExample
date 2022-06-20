package org.example.service

import org.example.model.TestEntity

interface TestService {
    fun saveEntity(entity: TestEntity)
}