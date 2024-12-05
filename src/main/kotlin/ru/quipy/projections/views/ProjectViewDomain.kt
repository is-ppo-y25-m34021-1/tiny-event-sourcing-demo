package ru.quipy.projections.views

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.quipy.domain.Unique
import java.util.*

class ProjectViewDomain {
    @Document("project-view")
    data class Project(
        @Id
        override val id: UUID,
        var name: String
    ) : Unique<UUID>
}