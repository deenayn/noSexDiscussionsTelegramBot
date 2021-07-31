package com.timewithoutsexbot.bot

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*
import java.util.concurrent.TimeUnit

class TimeWithoutSexBot(private val lastSexMessageDateRepository: LastSexMessageDateRepository) :
    TelegramLongPollingBot() {

    private val threshold = 600
    private val sexWords = arrayOf("sex", "секс", "няшит", "поебем", "девствен", "няшкаться", "няшкал", "ебат")

    override fun getBotToken(): String {
        return System.getenv("TELEGRAM_API_TOKEN")
    }

    override fun getBotUsername(): String {
        return "time_without_sex_bot"
    }

    override fun onUpdateReceived(update: Update?) {
        update?.let {
            if (it.hasMessage() && it.message.hasText() && hasSexWords(it.message.text)) {
                val lastSexMessage = lastSexMessageDateRepository.get(it.message.chatId)
                if (isFirstMessage(it.message, lastSexMessage)) {
                    sendMessage(it.message.chatId, "Снова разговоры о сексе!")
                } else if (lastSexMessage != null && isActual(it.message, lastSexMessage)) {
                    val minWithoutSex =
                        TimeUnit.MINUTES.convert((it.message.date - lastSexMessage).toLong(), TimeUnit.SECONDS)
                    sendMessage(it.message.chatId, "Жизнь без разговоров про секс: $minWithoutSex мин")
                }
                lastSexMessageDateRepository.set(it.message.chatId, it.message.date)
            }
        }
    }

    private fun isFirstMessage(message: Message, lastSexMessage: Int?) =
        lastSexMessage == null && message.date > (System.currentTimeMillis() / 1000) - 120

    private fun sendMessage(chatId: Long, text: String) {
        val sendMessage = SendMessage()
        sendMessage.chatId = chatId.toString()
        sendMessage.text = text
        execute(sendMessage)
    }

    private fun isActual(message: Message, lastSexMessage: Int): Boolean {
        return message.date > lastSexMessage + threshold
    }

    private fun hasSexWords(str: String): Boolean {
        val words = str.lowercase(Locale.getDefault()).split("\\P{L}+".toRegex())
        return words.any { word ->
            sexWords.any {
                word.contains(it)
            }
        }
    }
}
