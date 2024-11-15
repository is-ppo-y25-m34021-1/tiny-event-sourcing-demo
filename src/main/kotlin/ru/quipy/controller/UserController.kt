package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.project.CreatedProjectEvent
import ru.quipy.api.user.UserAggregate
import ru.quipy.api.user.UserCreatedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.dto.UserDto
import ru.quipy.logic.user.UserAggregateState
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>
) {
    @PostMapping("/createUser")
    fun createProject(@RequestBody userDto: UserDto) : UserCreatedEvent {
        return userEsService.create {
            it.createUser(UUID.randomUUID(), userDto) }
    }
}