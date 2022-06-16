package org.example.service

import org.example.model.TestEntity
import java.util.*

interface TestService {
    fun saveOrGetId(entity: TestEntity): UUID
}