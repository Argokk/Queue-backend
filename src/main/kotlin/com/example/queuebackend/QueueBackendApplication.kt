package com.example.queuebackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@SpringBootApplication
class QueueBackendApplication

fun main(args: Array<String>) {
    runApplication<QueueBackendApplication>(*args)
}
