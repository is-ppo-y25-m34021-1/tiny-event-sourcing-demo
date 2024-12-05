package ru.quipy.projections.views

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.quipy.domain.Unique
import java.util.*

class TaskViewDomain {
    @Document("task-view")
    data class Task(
        @Id
        override val id: UUID,
        var projectId: UUID,
        var taskName: String,
        var taskDescription: String,
        var statusName: String,
        var assigneeUsersId: MutableList<UUID> = mutableListOf(),
    ) : Unique<UUID>
}