package ru.quipy.api.project

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val CREATED_PROJECT_EVENT = "CREATED_PROJECT_EVENT"
const val CREATE_PROJECT_FAILED_EVENT = "CREATE_PROJECT_FAILED_EVENT"
const val ADDED_USER_TO_PROJECT_EVENT = "ADDED_USER_TO_PROJECT_EVENT"
const val REORDERED_STATUS_EVENT = "REORDERED_STATUS_EVENT"
const val CREATED_STATUS_EVENT = "CREATED_STATUS_EVENT"
const val DELETED_STATUS_EVENT = "DELETED_STATUS_EVENT"



@DomainEvent(name = CREATED_PROJECT_EVENT)
class ProjectCreatedEvent(
    val projectId: UUID,
    val title: String,
    val creatorId: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = CREATED_PROJECT_EVENT,
    createdAt = createdAt,
)

