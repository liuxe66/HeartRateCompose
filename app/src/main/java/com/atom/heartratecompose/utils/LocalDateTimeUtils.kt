package com.atom.heartratecompose.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


/**
 *  author : liuxe
 *  date : 2024/1/8 13:18
 *  description :
 */
fun LocalDateTime.format(): String {
    val now = LocalDateTime.now()
    val diffInDays = ChronoUnit.DAYS.between(this, now)

    return if (diffInDays == 0L) {
        "今天,${this.hour}:${this.minute}"
    } else if (diffInDays == 1L) {
        "昨天,${this.hour}:${this.minute}"
    }else {
        this.dayOfMonth
        this.monthValue
        this.year
        "${this.monthValue}月${this.dayOfMonth}日，${this.hour}:${this.minute}"
    }
}
