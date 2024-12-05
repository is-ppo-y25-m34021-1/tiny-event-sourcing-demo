package ru.quipy.projections.views

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.quipy.domain.Unique
import java.util.*

class UserViewDomain {
    @Document("user-view")
    data class User(
        @Id
        override val id: UUID,
        var nickname: String,
        val userName: String,
        val password: String,
        val projectsIds: MutableList<UUID> = mutableListOf()
    ) : Unique<UUID>
}