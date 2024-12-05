package ru.quipy.projections.service

import org.springframework.stereotype.Service
import ru.quipy.api.project.*
import ru.quipy.projections.repository.StatusRepository
import ru.quipy.projections.repository.TaskRepository
import ru.quipy.projections.views.TaskViewDomain
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

@Service
class TaskViewService(
    private val taskRepository: TaskRepository,
    private val statusRepository: StatusRepository,
    private val subscriptionsManager: AggregateSubscriptionsManager
) {
    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "task-event-stream") {
            `when`(CreatedTaskEvent::class) { event ->
                createTask(event)
            }

            `when`(RemovedTaskEvent::class) { event ->
                removeTask(event)
            }

            `when`(AssignedTaskEvent::class) { event ->
                assignTask(event)
            }

            `when`(UpdatedTaskNameEvent::class) { event ->
                updateTaskName(event)
            }

            `when`(UpdatedTaskDescriptionEvent::class) { event ->
                updateTaskDescription(event)
            }

            `when`(UpdatedTaskStatusEvent::class) { event ->
                updateTaskStatus(event)
            }
        }
    }

    private fun createTask(event: CreatedTaskEvent) {
        taskRepository.save(
            TaskViewDomain.Task(
                event.taskId,
                event.projectId,
                event.taskName,
                event.taskDescription,
                event.statusName,
                mutableListOf()
            )
        )
    }

    private fun removeTask(event: RemovedTaskEvent) {
        taskRepository.deleteById(event.taskId)
    }

    private fun assignTask(event: AssignedTaskEvent) {
        val task = taskRepository.findById(event.taskId).orElseThrow()
        task.assigneeUsersId.add(event.assigneeUserId)
        taskRepository.save(task)
    }

    private fun updateTaskName(event: UpdatedTaskNameEvent) {
        val task = taskRepository.findById(event.taskId).orElseThrow()
        task.taskName = event.taskName
        taskRepository.save(task)
    }

    private fun updateTaskDescription(event: UpdatedTaskDescriptionEvent) {
        val task = taskRepository.findById(event.taskId).orElseThrow()
        task.taskDescription = event.taskDescription
        taskRepository.save(task)
    }

    private fun updateTaskStatus(event: UpdatedTaskStatusEvent) {
        val task = taskRepository.findById(event.taskId).orElseThrow()
        statusRepository.findByStatusName(event.statusName) ?: throw IllegalArgumentException("Not found status")
        task.statusName = event.statusName
        taskRepository.save(task)
    }

    fun getTasksByProjectId(projectId: UUID): MutableIterable<TaskViewDomain.Task>? {
        return taskRepository.findByProjectId(projectId)
    }

    fun getTasksById(taskId: UUID): TaskViewDomain.Task? {
        return taskRepository.findById(taskId).orElseThrow()
    }

    fun getTasksByUserId(userId: UUID): List<TaskViewDomain.Task> {
        val tasks = taskRepository.findAll()
        return tasks.filter { task -> task.assigneeUsersId.contains(userId) }
    }
}