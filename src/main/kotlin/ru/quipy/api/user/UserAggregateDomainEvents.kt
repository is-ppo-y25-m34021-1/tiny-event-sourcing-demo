package ru.quipy.api.user

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val CREATED_USER_EVENT = "CREATED_USER_EVENT"

@DomainEvent(name = CREATED_USER_EVENT)
class UserCreatedEvent(
    val userId: UUID,
    val userName: String,
    val nickname: String,
    val password: String,
) : Event<UserAggregate>(
    name = CREATED_USER_EVENT
)