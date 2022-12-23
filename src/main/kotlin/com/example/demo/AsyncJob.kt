package com.example.demo

import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.io.Closeable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.persistence.EntityManagerFactory

@Component
final class AsyncJob(private val entityManagerFactory: EntityManagerFactory) : Closeable {
    private val logger = KotlinLogging.logger {}
    private val executorService = Executors.newFixedThreadPool(10)

    @PostConstruct
    fun postConstruct() {
        executorService.execute(this::doWork)
        executorService.execute(this::doWork)
        executorService.execute(this::doWork)
    }

    private fun doWork() {
        val random = (1..9).random()
        repeat(random * 100) { checkDb() }
        logger.info { "doWork ${entityManagerFactory.isOpen}" }
        executorService.execute(this::doWork)
    }

    private fun checkDb() {
        Thread.sleep(10)
        if (!entityManagerFactory.isOpen) {
            throw IllegalStateException("DB not available")
        }
    }

    override fun close() {
        logger.info { "Shut down executor service" }
        executorService.shutdown()
        executorService.awaitTermination(10, TimeUnit.SECONDS)
        logger.info { "Termination done: ${executorService.isTerminated}" }
    }
}
