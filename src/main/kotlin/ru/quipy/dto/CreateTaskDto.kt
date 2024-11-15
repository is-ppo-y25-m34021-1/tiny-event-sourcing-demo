package ru.quipy.dto

import java.util.*

data class CreateTaskDto(
    val taskName: String,
    val taskDescription: String,
    var assigneeUsersId: List<UUID>?
)
