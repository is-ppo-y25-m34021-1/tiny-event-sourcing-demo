package ru.quipy.api.project

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val CREATED_PROJECT_EVENT = "CREATED_PROJECT_EVENT"
const val ADDED_USER_TO_PROJECT_EVENT = "ADDED_USER_TO_PROJECT_EVENT"

const val CREATED_STATUS_EVENT = "CREATED_STATUS_EVENT"
const val REORDERED_STATUS_EVENT = "REORDERED_STATUS_EVENT"
const val REMOVED_STATUS_EVENT = "REMOVED_STATUS_EVENT"
const val CREATED_TASK_EVENT = "CREATED_TASK_EVENT"
const val REMOVED_TASK_EVENT = "REMOVED_TASK_EVENT"
const val ASSIGNED_TASK_EVENT = "ASSIGNED_TASK_EVENT"
const val UPDATED_TASK_NAME_EVENT = "UPDATED_TASK_NAME_EVENT"
const val UPDATED_TASK_DESCRIPTION_EVENT = "UPDATED_TASK_DESCRIPTION_EVENT"
const val UPDATED_TASK_STATUS_EVENT = "UPDATED_TASK_STATUS_EVENT"

@DomainEvent(name = CREATED_PROJECT_EVENT)
class CreatedProjectEvent(
    val projectId: UUID,
    val projectName: String,
    val createdById: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = CREATED_PROJECT_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = ADDED_USER_TO_PROJECT_EVENT)
class AddedUserToProjectEvent(
    val projectId: UUID,
    val userId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = ADDED_USER_TO_PROJECT_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = CREATED_STATUS_EVENT)
class CreatedStatusEvent(
    val projectId: UUID,
    val statusId: UUID,
    val statusName: String,
    val statusColor: String,
    var orderNumber: Int,
    createdAt: Long = System.currentTimeMillis()
) : Event<ProjectAggregate>(
    name = CREATED_STATUS_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = REMOVED_STATUS_EVENT)
class RemoveStatusEvent(
    val statusName: String,
    val projectId: UUID
) : Event<ProjectAggregate>(
    name = REMOVED_STATUS_EVENT,
)

@DomainEvent(name = CREATED_TASK_EVENT)
class CreatedTaskEvent(
    val taskId: UUID,
    val projectId: UUID,
    val taskName: String,
    val taskDescription: String,
    val statusName: String,
    var assigneeUsersId: List<UUID>?,
    createdAt: Long = System.currentTimeMillis()
) : Event<ProjectAggregate>(
    name = CREATED_TASK_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = REMOVED_TASK_EVENT)
class RemovedTaskEvent(
    val taskId: UUID,
    val projectId: UUID,
    createdAt: Long = System.currentTimeMillis()
) : Event<ProjectAggregate>(
    name = REMOVED_TASK_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = ASSIGNED_TASK_EVENT)
class AssignedTaskEvent(
    val projectId: UUID,
    val taskId: UUID,
    val assigneeUserId: UUID
) : Event<ProjectAggregate>(
    name = ASSIGNED_TASK_EVENT,
)

@DomainEvent(name = UPDATED_TASK_NAME_EVENT)
class UpdatedTaskNameEvent(
    val projectId: UUID,
    val taskId: UUID,
    val taskName: String
) : Event<ProjectAggregate>(
    name = UPDATED_TASK_NAME_EVENT,
)

@DomainEvent(name = UPDATED_TASK_DESCRIPTION_EVENT)
class UpdatedTaskDescriptionEvent(
    val projectId: UUID,
    val taskId: UUID,
    val taskDescription: String
) : Event<ProjectAggregate>(
    name = UPDATED_TASK_DESCRIPTION_EVENT,
)

@DomainEvent(name = UPDATED_TASK_STATUS_EVENT)
class UpdatedTaskStatusEvent(
    val projectId: UUID,
    val taskId: UUID,
    val statusName: String
) : Event<ProjectAggregate>(
    name = UPDATED_TASK_STATUS_EVENT,
)