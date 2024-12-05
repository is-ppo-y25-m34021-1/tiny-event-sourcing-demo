package ru.quipy.projections.service

import org.springframework.stereotype.Service
import ru.quipy.api.project.CreatedStatusEvent
import ru.quipy.api.project.ProjectAggregate
import ru.quipy.api.project.RemoveStatusEvent
import ru.quipy.projections.repository.StatusRepository
import ru.quipy.projections.views.StatusViewDomain
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

@Service
class StatusViewService(
    private val statusRepository: StatusRepository,
    private val subscriptionsManager: AggregateSubscriptionsManager
) {

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "status-event-stream") {
            `when`(CreatedStatusEvent::class) { event ->
                createStatus(event)
            }

            `when`(RemoveStatusEvent::class) { event ->
                removeStatus(event)
            }
        }
    }

    private fun createStatus(event: CreatedStatusEvent) {
        statusRepository.save(
            StatusViewDomain.Status(
                event.statusId, event.projectId, event.statusName, event.statusColor, event.orderNumber
            )
        )
    }

    private fun removeStatus(event: RemoveStatusEvent) {
        val status = statusRepository.findByStatusName(event.statusName)
        if (status != null) {
            statusRepository.delete(status)
        }
    }

    fun getStatusesByProjectId(projectId: UUID): MutableIterable<StatusViewDomain.Status>? {
        return statusRepository.findByProjectId(projectId)
    }
}