package com.lira

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LiraApplication

fun main(args: Array<String>) {
    runApplication<LiraApplication>(*args)
}
