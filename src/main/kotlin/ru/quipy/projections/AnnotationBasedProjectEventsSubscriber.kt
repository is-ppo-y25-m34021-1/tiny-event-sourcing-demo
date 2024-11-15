package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.api.project.*
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(
    aggregateClass = ProjectAggregate::class, subscriberName = "demo-subs-stream"
)
class AnnotationBasedProjectEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(AnnotationBasedProjectEventsSubscriber::class.java)

    @SubscribeEvent
    fun projectCreateEvent(event: CreatedProjectEvent) {
        logger.info("Project created: {}", event.projectName)
    }

    @SubscribeEvent
    fun taskCreatedEvent(event: CreatedTaskEvent) {
        logger.info("Task created: {}", event.taskName)
    }

    @SubscribeEvent
    fun statusCreatedEvent(event: CreatedStatusEvent) {
        logger.info("Status created: {}", event.name)
    }
}