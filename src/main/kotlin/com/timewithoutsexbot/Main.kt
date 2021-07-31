package com.timewithoutsexbot

import com.timewithoutsexbot.bot.LastSexMessageDateRepository
import com.timewithoutsexbot.bot.TimeWithoutSexBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

fun main() {
    try {
        val botApi = TelegramBotsApi(DefaultBotSession::class.java)
        botApi.registerBot(TimeWithoutSexBot(LastSexMessageDateRepository()))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
