# Gracefully shutdown spring boot application

In some situations DB connection is shutdown before some worker.
I encountered this when having a pub-sub listener. It was still consuming messages and wanted to write to DB when the connection is already closed.
This leads to the following exception

    2022-12-23 11:12:18.710  INFO 56117 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
    Exception in thread "pool-1-thread-5" java.lang.IllegalStateException: DB not available
        at com.example.demo.AsyncJob.checkDb(AsyncJob.kt:33)
        at com.example.demo.AsyncJob.doWork(AsyncJob.kt:25)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:833)


Implementing `Closeable` allows to gracefully shutdown and avoid the exception.

To reproduce the problem and see the exception
- remove implementation of `Closeable` from `AsyncJob`
- start the application
- stop it after a little while