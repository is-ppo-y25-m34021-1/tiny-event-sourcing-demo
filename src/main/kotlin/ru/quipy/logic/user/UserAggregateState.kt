package ru.quipy.logic.user

import ru.quipy.api.user.UserAggregate
import ru.quipy.api.user.UserCreatedEvent
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import ru.quipy.dto.UserDto
import java.util.*

class UserAggregateState : AggregateState<UUID, UserAggregate> {
    private lateinit var userId: UUID
    lateinit var userName: String
    lateinit var nickname: String
    lateinit var password: String
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    override fun getId(): UUID = userId

    fun createUser(userId: UUID, userDto: UserDto) : UserCreatedEvent {
        return UserCreatedEvent(
            userId = userId,
            userName = userDto.userName,
            nickname = userDto.nickname,
            password = userDto.password,
        )
    }

    @StateTransitionFunc
    fun createUser(event: UserCreatedEvent){
        userId = event.userId
        userName = event.userName
        nickname = event.nickname
        password = event.password
    }
}