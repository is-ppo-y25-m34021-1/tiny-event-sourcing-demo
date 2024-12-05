package ru.quipy.projections.service

import org.springframework.stereotype.Service
import ru.quipy.api.user.UserAggregate
import ru.quipy.api.user.UserCreatedEvent
import ru.quipy.projections.repository.ProjectRepository
import ru.quipy.projections.repository.UserRepository
import ru.quipy.projections.views.ProjectViewDomain
import ru.quipy.projections.views.UserViewDomain
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

@Service
class UserViewService(
    private val projectRepository: ProjectRepository,
    private val subscriptionsManager: AggregateSubscriptionsManager,
    private val userRepository: UserRepository
) {
    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(UserAggregate::class, "user-event-stream") {
            `when`(UserCreatedEvent::class) { event ->
                createUser(event)
            }
        }
    }

    private fun createUser(event: UserCreatedEvent) {
        userRepository.save(
            UserViewDomain.User(
                event.userId,
                event.nickname,
                event.userName,
                event.password,
                arrayListOf()
            )
        )
    }

    fun getProjectsByUserId(userId: UUID): MutableIterable<ProjectViewDomain.Project> {
        val user = userRepository.findById(userId).orElseThrow()
        return projectRepository.findAllById(user.projectsIds)
    }

    fun getUser(userName: String): UserViewDomain.User? {
        return userRepository.findByUserName(userName)
    }
}