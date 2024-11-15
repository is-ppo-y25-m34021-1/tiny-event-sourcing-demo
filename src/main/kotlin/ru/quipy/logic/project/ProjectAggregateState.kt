package ru.quipy.logic.project

import ru.quipy.api.project.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import ru.quipy.dto.*
import java.util.*

class ProjectAggregateState : AggregateState<UUID, ProjectAggregate> {
    private val defaultStatusName = "CREATED"
    private val defaultStatusColor = Color.RED
    private var maxOrderStatusNumber = 2

    private lateinit var projectId: UUID
    lateinit var projectName: String
    lateinit var createdById: UUID

    var tasks = mutableListOf<Task>()
    var participantIds = mutableSetOf<UUID>()
    var statuses = mutableMapOf<Int, Status>()

    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    override fun getId() = projectId

    fun createProject(id: UUID, projectName: String, creatorId: UUID): CreatedProjectEvent {
        return CreatedProjectEvent(
            projectId = id,
            projectName = projectName,
            createdById = creatorId,
        )
    }

    @StateTransitionFunc
    fun createProject(event: CreatedProjectEvent) {
        projectId = event.projectId
        projectName = event.projectName
        createdById = event.createdById
        participantIds.add(event.createdById)
    }

    fun addParticipantToProject(projectId: UUID, userId: UUID): AddedUserToProjectEvent {
        return AddedUserToProjectEvent(
            projectId = projectId,
            userId = userId
        )
    }

    @StateTransitionFunc
    fun addParticipantToProject(event: AddedUserToProjectEvent) {
        participantIds.add(event.userId)
    }

    fun createStatus(projectId: UUID, newStatus: StatusDto): CreatedStatusEvent {
        if (this.statuses.values.find { status ->
                status.name == newStatus.statusName
                        && status.projectId == projectId
            } != null) {
            throw IllegalArgumentException("Status with name ${newStatus.statusName} already exists")
        }

        if (this.statuses.values.find { status ->
                status.color.name == newStatus.statusColor
                        && status.projectId == projectId
            } != null) {
            throw IllegalArgumentException("Status with color ${newStatus.statusColor} already exists")
        }

        return CreatedStatusEvent(
            projectId = projectId,
            statusId = UUID.randomUUID(),
            statusName = newStatus.statusName,
            statusColor = newStatus.statusColor,
            orderNumber = maxOrderStatusNumber++
        )
    }

    @StateTransitionFunc
    fun createStatus(event: CreatedStatusEvent) {
        statuses[event.orderNumber] = Status(
            id = event.statusId,
            name = event.statusName,
            projectId = event.projectId,
            color = Color.valueOf(event.statusColor),
        )
    }

    fun removeStatus(projectId: UUID, statusName: String): RemoveStatusEvent {
        if (this.tasks.find { task ->
                task.statusName == statusName
            } != null) {
            throw IllegalArgumentException("There are tasks with status name $statusName")
        }

        return RemoveStatusEvent(
            statusName = statusName,
            projectId = projectId
        )
    }

    @StateTransitionFunc
    fun removeStatus(event: RemoveStatusEvent) {
        statuses.remove(
            statuses.filterValues { status ->
                status.name == event.statusName
                        && status.projectId == event.projectId
            }.keys.first()
        )
    }

    fun createTask(projectId: UUID, task: CreateTaskDto): CreatedTaskEvent {
        return CreatedTaskEvent(
            taskId = UUID.randomUUID(),
            projectId = projectId,
            taskName = task.taskName,
            taskDescription = task.taskDescription,
            statusName = defaultStatusName,
            assigneeUsersId = task.assigneeUsersId
        )
    }

    @StateTransitionFunc
    fun createTask(event: CreatedTaskEvent) {
        tasks.add(
            Task(
                id = event.taskId,
                taskName = event.taskName,
                taskDescription = event.taskDescription,
                statusName = event.statusName,
                assigneeUsersId = event.assigneeUsersId as MutableList<UUID>?
            )
        )
    }

    fun assignTask(assignTaskDto: AssignTaskDto): AssignedTaskEvent {
        val existTask = tasks.find { task -> task.id == assignTaskDto.taskId }
        if (existTask == null) {
            throw IllegalArgumentException("Task with id ${assignTaskDto.taskId} not found")
        }
        if (existTask.assigneeUsersId?.contains(assignTaskDto.assigneeUserId) == true) {
            throw IllegalArgumentException(
                "User with id ${assignTaskDto.assigneeUserId} already exists in assignee users"
            )
        }

        return AssignedTaskEvent(
            projectId = assignTaskDto.projectId,
            taskId = assignTaskDto.projectId,
            assigneeUserId = assignTaskDto.assigneeUserId,
        )
    }

    @StateTransitionFunc
    fun assignTask(event: AssignedTaskEvent) {
        val existTask = tasks.find { task -> task.id == event.taskId }
        existTask?.assigneeUsersId?.add(event.assigneeUserId);
    }

    fun removeTask(removeTaskDto: RemoveTaskDto):RemovedTaskEvent {
        return RemovedTaskEvent(
            taskId = removeTaskDto.taskId,
            projectId = removeTaskDto.projectId,
        )
    }

    @StateTransitionFunc
    fun removeTask(event: RemovedTaskEvent) {
        val existTask = tasks.find { task -> task.id == event.taskId }
        tasks.remove(existTask)
    }

    fun updateTaskName(projectId: UUID, taskId: UUID, newTaskName: String) : UpdatedTaskNameEvent {
        return UpdatedTaskNameEvent(
            projectId = projectId,
            taskId = taskId,
            taskName = newTaskName
        )
    }

    @StateTransitionFunc
    fun updateTaskName(event: UpdatedTaskNameEvent) {
        val existTask = tasks.find { task -> task.id == event.taskId }
        if (existTask == null) {
            throw IllegalArgumentException("Task with id ${event.taskId} not found")
        }

        existTask.taskName = event.taskName
    }

    fun updateTaskDescription(projectId: UUID, taskId: UUID, newTaskDescription: String) : UpdatedTaskDescriptionEvent {
        return UpdatedTaskDescriptionEvent(
            projectId = projectId,
            taskId = taskId,
            taskDescription = newTaskDescription
        )
    }

    @StateTransitionFunc
    fun updateTaskDescription(event: UpdatedTaskDescriptionEvent) {
        val existTask = tasks.find { task -> task.id == event.taskId }
        if (existTask == null) {
            throw IllegalArgumentException("Task with id ${event.taskId} not found")
        }

        existTask.taskDescription = event.taskDescription
    }

    fun updateTaskStatus(projectId: UUID, taskId: UUID, newStatus: String) : UpdatedTaskStatusEvent {
        val existStatus = statuses.values.find { status -> status.name == newStatus }
        if (existStatus == null) {
            throw IllegalArgumentException("Status with name $newStatus not found")
        }

        return UpdatedTaskStatusEvent (
            projectId = projectId,
            taskId = taskId,
            statusName = newStatus
        )
    }

    @StateTransitionFunc
    fun updateTaskStatus(event: UpdatedTaskStatusEvent) {
        val existTask = tasks.find { task -> task.id == event.taskId }
        if (existTask == null) {
            throw IllegalArgumentException("Task with id ${event.taskId} not found")
        }

        existTask.statusName = event.statusName
    }

    }


data class Status(
    val id: UUID,
    val name: String,
    val projectId: UUID,
    val color: Color
)

data class Task(
    val id: UUID = UUID.randomUUID(),
    var taskName: String,
    var taskDescription: String,
    var statusName: String,
    var assigneeUsersId: MutableList<UUID>?
)

enum class Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET
}