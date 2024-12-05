package ru.quipy.projections.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.quipy.projections.views.UserViewDomain
import java.util.*

@Repository
interface UserRepository : MongoRepository<UserViewDomain.User, UUID> {
    fun findByUserName(userName: String): UserViewDomain.User?
}