package ru.abtank.botkotlinwebhook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BotKotlinWebhookApplication

fun main(args: Array<String>) {

    runApplication<BotKotlinWebhookApplication>(*args)
}
