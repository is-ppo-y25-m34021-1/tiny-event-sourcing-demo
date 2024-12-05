package ru.quipy.projections.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.quipy.projections.views.TaskViewDomain
import java.util.*

@Repository
interface TaskRepository : MongoRepository<TaskViewDomain.Task, UUID> {
    fun findByProjectId(projectId: UUID): MutableIterable<TaskViewDomain.Task>?
}