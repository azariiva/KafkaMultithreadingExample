package org.example.model

import java.nio.charset.StandardCharsets.UTF_8
import java.util.*

data class TestEntity(
    val id: UUID? = null,
    val value: String? = null,
) {
    fun generateId(): UUID {
        val sb: StringBuilder  = StringBuilder()
        // use all unique properties during id generation
        value?.let { sb.append(it) }
        return UUID.nameUUIDFromBytes(sb.toString().toByteArray(UTF_8))
    }
}
