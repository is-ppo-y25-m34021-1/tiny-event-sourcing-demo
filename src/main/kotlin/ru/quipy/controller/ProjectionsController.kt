package ru.quipy.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.quipy.projections.service.ProjectionsService
import ru.quipy.projections.views.ProjectViewDomain
import ru.quipy.projections.views.StatusViewDomain
import ru.quipy.projections.views.TaskViewDomain
import ru.quipy.projections.views.UserViewDomain
import java.util.*


@RestController
@RequestMapping("/aggregate")
class ProjectionsController(
    var projectionsService: ProjectionsService
) {
    @GetMapping("/project/{projectId}")
    fun getProjectsById(@PathVariable projectId: UUID): ProjectViewDomain.Project {
        return projectionsService.getProjectsById(projectId)
    }

    @GetMapping("/projects-by-user-id/{userId}")
    fun getProjectsByUserId(@PathVariable userId: UUID): MutableIterable<ProjectViewDomain.Project> {
        return projectionsService.getProjectsByUserId(userId)
    }

    @GetMapping("/task/{taskId}")
    fun getTasksById(@PathVariable taskId: UUID): TaskViewDomain.Task? {
        return projectionsService.getTasksById(taskId)
    }

    @GetMapping("/tasks-by-project-id/{projectId}")
    fun getTasksByProjectId(@PathVariable projectId: UUID): MutableIterable<TaskViewDomain.Task>? {
        return projectionsService.getTasksByProjectId(projectId)
    }

    @GetMapping("/tasks-by-user-id/{userId}")
    fun getTasksByUserId(@PathVariable userId: UUID): List<TaskViewDomain.Task>? {
        return projectionsService.getTasksByUserId(userId)
    }

    @GetMapping("/user/{userName}")
    fun getUserInfo(@PathVariable userName: String): UserViewDomain.User? {
        return projectionsService.getUser(userName)
    }

    @GetMapping("/statuses/{projectId}")
    fun getStatusesByProjectId(@PathVariable projectId: UUID): MutableIterable<StatusViewDomain.Status>? {
        return projectionsService.getStatusesByProjectId(projectId)
    }

}