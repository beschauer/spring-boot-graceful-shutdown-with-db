package com.example.demo

import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.persistence.EntityManagerFactory

@Component
class ScheduledJob(val entityManagerFactory: EntityManagerFactory) {
    private val logger = KotlinLogging.logger {}

    @Scheduled(fixedDelay = 500, timeUnit = TimeUnit.MILLISECONDS)
    fun foo() {
        Thread.sleep(1000)
        logger.info { "foo ${entityManagerFactory.isOpen}" }
        if (!entityManagerFactory.isOpen) {
            throw IllegalStateException("DB not available")
        }
    }

    @Scheduled(fixedDelay = 500, timeUnit = TimeUnit.MILLISECONDS, initialDelay = 100)
    fun bar() {
        Thread.sleep(1000)
        logger.info { "bar ${entityManagerFactory.isOpen}" }
        if (!entityManagerFactory.isOpen) {
            throw IllegalStateException("DB not available")
        }
    }

    @Scheduled(fixedDelay = 500, timeUnit = TimeUnit.MILLISECONDS, initialDelay = 200)
    fun baz() {
        Thread.sleep(1000)
        logger.info { "baz ${entityManagerFactory.isOpen}" }
        if (!entityManagerFactory.isOpen) {
            throw IllegalStateException("DB not available")
        }
    }
}
