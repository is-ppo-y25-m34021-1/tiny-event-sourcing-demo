package ru.quipy.projections.service

import org.springframework.stereotype.Service
import ru.quipy.projections.views.ProjectViewDomain
import ru.quipy.projections.views.StatusViewDomain
import ru.quipy.projections.views.TaskViewDomain
import ru.quipy.projections.views.UserViewDomain
import java.util.*

@Service
class ProjectionsService(
    private val projectViewService: ProjectViewService,
    private val userViewService: UserViewService,
    private val taskViewService: TaskViewService,
    private val statusViewService: StatusViewService

) {
    fun getProjectsById(projectId: UUID): ProjectViewDomain.Project {
        return projectViewService.getProjectsById(projectId)
    }

    fun getProjectsByUserId(userId: UUID): MutableIterable<ProjectViewDomain.Project> {
        return userViewService.getProjectsByUserId(userId)
    }

    fun getTasksById(taskId: UUID): TaskViewDomain.Task? {
        return taskViewService.getTasksById(taskId)
    }

    fun getTasksByProjectId(projectId: UUID): MutableIterable<TaskViewDomain.Task>? {
        return taskViewService.getTasksByProjectId(projectId)
    }

    fun getTasksByUserId(userId: UUID): List<TaskViewDomain.Task> {
        return taskViewService.getTasksByUserId(userId)
    }

    fun getUser(userName: String): UserViewDomain.User? {
        return userViewService.getUser(userName)
    }

    fun getStatusesByProjectId(projectId: UUID): MutableIterable<StatusViewDomain.Status>? {
        return statusViewService.getStatusesByProjectId(projectId)
    }

}