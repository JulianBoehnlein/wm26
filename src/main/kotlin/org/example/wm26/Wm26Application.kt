package org.example.wm26

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class Wm26Application

fun main(args: Array<String>) {
    runApplication<Wm26Application>(*args)
}
