package ru.quipy.projections.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.quipy.projections.views.StatusViewDomain
import java.util.*

@Repository
interface StatusRepository : MongoRepository<StatusViewDomain.Status, UUID> {
    fun findByStatusName(statusName: String): StatusViewDomain.Status?

    fun findByProjectId(projectId: UUID): MutableIterable<StatusViewDomain.Status>?
}