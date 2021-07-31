package com.timewithoutsexbot.bot

class LastSexMessageDateRepository {

    private val lastSexMessageMap = HashMap<Long, Int>()

    fun set(chatId: Long, newLastDate: Int) {
        lastSexMessageMap[chatId] = newLastDate
    }

    fun get(chatId: Long): Int? {
        return lastSexMessageMap[chatId]
    }
}
