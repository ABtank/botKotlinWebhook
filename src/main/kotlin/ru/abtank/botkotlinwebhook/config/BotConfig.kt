package ru.abtank.botkotlinwebhook.config

import lombok.Getter
import lombok.Setter
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.bots.DefaultBotOptions
import ru.abtank.botkotlinwebhook.WebhookBot

@Getter
@Setter
@Configuration
class BotConfig {

    private val proxyType: DefaultBotOptions.ProxyType = DefaultBotOptions.ProxyType.SOCKS5
    private val proxyHost = "localhost"
    private val proxyPort = 8289

    @Bean
    fun bot(): WebhookBot {
        var options = DefaultBotOptions()
        options.proxyHost = proxyHost
        options.proxyPort = proxyPort
        options.proxyType = proxyType
//        return WebhookBot(options)
        return WebhookBot(DefaultBotOptions())
    }
}