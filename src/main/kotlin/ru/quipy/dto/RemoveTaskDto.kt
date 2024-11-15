package ru.quipy.dto

import java.util.*

data class RemoveTaskDto(
    val projectId: UUID,
    val taskId: UUID
)