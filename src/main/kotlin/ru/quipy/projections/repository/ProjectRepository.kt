package ru.quipy.projections.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.quipy.projections.views.ProjectViewDomain
import java.util.*

@Repository
interface ProjectRepository : MongoRepository<ProjectViewDomain.Project, UUID> {
}