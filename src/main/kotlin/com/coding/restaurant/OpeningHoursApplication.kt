package com.coding.restaurant

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OpeningHoursApplication

fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    runApplication<OpeningHoursApplication>(*args)
}
