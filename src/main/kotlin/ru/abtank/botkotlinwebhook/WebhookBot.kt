package ru.abtank.botkotlinwebhook

import ru.abtank.botkotlinwebhook.handler.ButtonHendler
import ru.abtank.botkotlinwebhook.handler.GsonParser
import ru.abtank.botkotlinwebhook.handler.HttpHendler
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramWebhookBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

// Установка webhook
// GET https://api.telegram.org/bot2134033398:AAFH9V8VFAaY-p83vRgNG2nn7BZfoAl5MdQ/setWebhook?url=https://967c-188-242-33-154.ngrok.io
class WebhookBot : TelegramWebhookBot {
    constructor(option: DefaultBotOptions) : super(option)

    private var BOT_NAME = "KotlinWebhookBot"
    private var BOT_TOKEN = "2134033398:AAFH9V8VFAaY-p83vRgNG2nn7BZfoAl5MdQ"
    private var API_Path = "http://localhost:8189/fitnessab"
    private var BOT_Path = "https://967c-188-242-33-154.ngrok.io"
    private val httpHendler = HttpHendler()
    private val gsonParser = GsonParser()
    private val buttonHendler = ButtonHendler()

    override fun getBotToken() = BOT_TOKEN
    override fun getBotUsername() = BOT_NAME
    override fun getBotPath() = BOT_Path

    override fun onWebhookUpdateReceived(update: Update): BotApiMethod<*>? {
        val chatIdLong: Long = update.message.getChatId()

//        действие по кнопке старт
        if (update.message.text == "/auth") {
            var sb = StringBuilder()
            sb.append("OK.\nОтправь мне login и password.\n")
            sb.append("\nВаш пароль должен содержать цифры, буквы, знаки пунктуации, завязку, развитие, кульминацию и неожиданный финал.\n")
            sb.append("\nПожалуйста используй данный формат:\n")
            sb.append("login - password")
            try {
                val deleteMessage = DeleteMessage(chatIdLong.toString(), update.message.messageId)
                execute(deleteMessage);
                execute(SendMessage.builder()
                        .chatId(chatIdLong.toString())
                        .text(sb.toString())
                        .build())
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }

        } else if (update.message.text == "/help") {
            var sb = StringBuilder()
            sb.append("/help - все команды\n/auth - авторизация в системе\n/cbtn - клавиатура\n/linkbtn - ссылки\n/clear - удалить клаву\n/category - все категории")
            execute(SendMessage.builder()
                    .chatId(chatIdLong.toString())
                    .text(sb.toString())
                    .build())

        } else if (update.message.text == "/category") {
            var sb = StringBuilder()
//            val categoryDto = gsonParser.parserCategory(httpHendler.printHttp("$API_Path/api/v1/category/1"))
            val categories = gsonParser.parserCategories(httpHendler.printHttp("$API_Path/api/v1/category"))
            for (o in categories) {
                sb.append("\n").append("${o.name}").append("\n")
            }

            execute(SendMessage.builder()
                    .chatId(chatIdLong.toString())
                    .text(sb.toString())
                    .build())

        } else if (update.message.text == "/clear") {
            execute((buttonHendler.sendClearCustomKeyboard(chatIdLong.toString())))

        } else if (update.message.text == "/linkbtn") {
            execute((buttonHendler.sendInlineKeyboard(chatIdLong.toString())))

        } else if (update.message.text == "/cbtn") {
            execute((buttonHendler.sendCustomKeyboard(chatIdLong.toString())))

        } else if (update.hasMessage()) {
            println("hasText = ${update.message.hasText()}")
            println("hasPhoto = ${update.message.hasPhoto()}")
            if (update.message.hasText()) {
                try {
                    execute(SendMessage.builder()
                            .chatId(chatIdLong.toString())
                            .text("Че?\nЯ не расслышал.")
                            .build())

                    execute(SendMessage.builder()
                            .chatId(chatIdLong.toString())
                            .text("${update.message.text}?")
                            .build())

                    execute(SendMessage.builder()
                            .chatId(chatIdLong.toString())
                            .text("Не важно.\nНичего вам не скажу.\nВы все равно не зарегистрированны.\n")
                            .build())
                } catch (e: TelegramApiException) {
                    e.printStackTrace()
                }
            } else if (update.message.hasPhoto()) {
                execute(SendMessage.builder()
                        .chatId(chatIdLong.toString())
                        .text("Моя твоя фото не понимать.")
                        .build())
            }
        }
        return null
    }

}