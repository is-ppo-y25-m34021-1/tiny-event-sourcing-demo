package ru.quipy.projections.views

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.quipy.domain.Unique
import java.util.*

class StatusViewDomain {
    @Document("status-view")
    data class Status(
        @Id
        override val id: UUID,
        var projectId: UUID,
        var statusName: String,
        var statusColor: String,
        var orderNumber: Int
    ) : Unique<UUID>
}