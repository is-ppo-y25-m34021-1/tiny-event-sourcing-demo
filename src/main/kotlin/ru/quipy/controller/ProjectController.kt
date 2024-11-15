package ru.quipy.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.project.*
import ru.quipy.core.EventSourcingService
import ru.quipy.dto.*
import ru.quipy.logic.project.ProjectAggregateState
import java.util.*

@RestController
@RequestMapping("/projects")
class ProjectController(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>,
) {

    @PostMapping("/{projectTitle}")
    fun createProject(@PathVariable projectTitle: String, @RequestParam creatorId: UUID) : CreatedProjectEvent {
        return projectEsService.create {
            it.createProject(UUID.randomUUID(), projectTitle, creatorId) }
    }

    @GetMapping("/{projectId}")
    fun getAccount(@PathVariable projectId: UUID) : ProjectAggregateState? {
        return projectEsService.getState(projectId)
    }

    @PostMapping("/add-participant/{projectId}/{userId}")
    fun addParticipant(@PathVariable projectId: UUID, @PathVariable userId: UUID) : AddedUserToProjectEvent {
        return projectEsService.update(projectId) {
            it.addParticipantToProject(projectId, userId)
        }
    }

    @PostMapping("/create-status/{projectId}")
    fun createProjectStatus(@PathVariable projectId: UUID, @RequestBody status: StatusDto): CreatedStatusEvent {
        return projectEsService.update(projectId) {
            it.createStatus(projectId, status)
        }
    }

    @DeleteMapping("/remove-status/{projectId}/{statusName}")
    fun removeProjectStatus(@PathVariable projectId: UUID, @PathVariable statusName: String): RemoveStatusEvent {
        return projectEsService.update(projectId) {
            it.removeStatus(projectId, statusName)
        }
    }

    @PostMapping("/create-task/{projectId}")
    fun createTask(@PathVariable projectId: UUID, @RequestBody task: CreateTaskDto) : CreatedTaskEvent {
        return projectEsService.update(projectId) {
            it.createTask(projectId, task)
        }
    }

    @PutMapping("/assign-task")
    fun assignTask(@RequestBody assignTaskDto: AssignTaskDto) : AssignedTaskEvent {
        return projectEsService.update(assignTaskDto.projectId) {
            it.assignTask(assignTaskDto)
        }
    }

    @DeleteMapping("/remove-task")
    fun removeTask(@RequestBody removeTaskDto: RemoveTaskDto) : RemovedTaskEvent {
        return projectEsService.update(removeTaskDto.projectId) {
            it.removeTask(removeTaskDto)
        }
    }

    @PutMapping("/{projectId}/update-task-name/{taskId}")
    fun updateTaskName(@PathVariable projectId: UUID, @PathVariable taskId: UUID,
                       @RequestParam newTaskName : String): UpdatedTaskNameEvent {
        return projectEsService.update(projectId) {
            it.updateTaskName(projectId, taskId, newTaskName)
        }
    }

    @PutMapping("/{projectId}/update-task-description/{taskId}")
    fun updateTaskDescription(@PathVariable projectId: UUID, @PathVariable taskId: UUID,
                       @RequestParam newTaskDescription : String): UpdatedTaskDescriptionEvent {
        return projectEsService.update(projectId) {
            it.updateTaskDescription(projectId, taskId, newTaskDescription)
        }
    }

    @PutMapping("/{projectId}/update-task-status/{taskId}")
    fun updateTaskStatus(@PathVariable projectId: UUID, @PathVariable taskId: UUID,
                              @RequestParam newTaskStatus : String): UpdatedTaskStatusEvent {
        return projectEsService.update(projectId) {
            it.updateTaskStatus(projectId, taskId, newTaskStatus)
        }
    }
//    @PostMapping("/{projectId}/tasks/{taskName}")
//    fun createTask(@PathVariable projectId: UUID, @PathVariable taskName: String) : TaskCreatedEvent {
//        return projectEsService.update(projectId) {
//            it.addTask(taskName)
//        }
//    }
}