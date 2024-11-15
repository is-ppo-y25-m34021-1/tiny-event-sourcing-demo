package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.api.project.*
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Service
class ProjectEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(ProjectEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "some-meaningful-name") {

            `when`(CreatedProjectEvent::class) { event ->
                logger.info("Project created: {}", event.name)
            }

            `when`(CreatedTaskEvent::class) { event ->
                logger.info("Task created: {}", event.name)
            }

            `when`(CreatedStatusEvent::class) { event ->
                logger.info("Status created: {}", event.name)
            }
        }
    }
}