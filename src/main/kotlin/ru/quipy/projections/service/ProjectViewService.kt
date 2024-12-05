package ru.quipy.projections.service

import org.springframework.stereotype.Service
import ru.quipy.api.project.AddedUserToProjectEvent
import ru.quipy.api.project.CreatedProjectEvent
import ru.quipy.api.project.ProjectAggregate
import ru.quipy.projections.repository.ProjectRepository
import ru.quipy.projections.repository.UserRepository
import ru.quipy.projections.views.ProjectViewDomain
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

@Service
class ProjectViewService(
    private val projectRepository: ProjectRepository,
    private val subscriptionsManager: AggregateSubscriptionsManager,
    private val userRepository: UserRepository
) {
    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "projects-event-stream") {
            `when`(CreatedProjectEvent::class) { event ->
                createProject(event)
            }

            `when`(AddedUserToProjectEvent::class) { event ->
                addUserToProject(event)
            }
        }
    }

    private fun addUserToProject(event: AddedUserToProjectEvent) {
        val user = userRepository.findById(event.userId).orElseThrow()
        var project = projectRepository.findById(event.projectId).orElseThrow()
        user.projectsIds.add(event.projectId)
        userRepository.save(user)
    }

    private fun createProject(event: CreatedProjectEvent) {
        projectRepository.save(
            ProjectViewDomain.Project(
                event.projectId, event.projectName
            )
        )
    }

    fun getProjectsById(projectId: UUID): ProjectViewDomain.Project {
        return projectRepository.findById(projectId).orElseThrow()
    }
}