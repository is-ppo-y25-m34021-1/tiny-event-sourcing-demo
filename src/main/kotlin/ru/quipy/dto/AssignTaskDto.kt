package ru.quipy.dto

import java.util.*

data class AssignTaskDto(
    val projectId: UUID,
    val taskId: UUID,
    var assigneeUserId: UUID
)