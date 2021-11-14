package ru.abtank.botkotlinwebhook

import org.springframework.web.bind.annotation.*
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update


@RestController ("/")
class WebhookController(private val bot: WebhookBot) {

    @PostMapping
    fun onUpdateReceived(@RequestBody update: Update?): BotApiMethod<*>? {
        return update?.let { bot.onWebhookUpdateReceived(it) }
    }
}