package com.atom.heartratecompose.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

/**
 *  author : liuxe
 *  date : 2024/1/5 16:51
 *  description :
 */

@Entity(tableName = "heart_rate")
data class HeartRateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var date: LocalDateTime = LocalDateTime.now(),
    var heartRate: Int = 0,
    var state: Int = 0
)