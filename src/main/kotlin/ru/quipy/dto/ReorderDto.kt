package ru.quipy.dto

import java.util.*

data class ReorderDto(
    val projectId: UUID,
    val statusName: String,
    var newOrder: Int
)
