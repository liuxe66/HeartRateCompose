package com.atom.heartratecompose.repo

import com.atom.heartratecompose.app.HeartRateApp
import com.atom.heartratecompose.db.entity.HeartRateEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime

/**
 *  author : liuxe
 *  date : 2024/1/5 17:10
 *  description :
 */
class AppRepo {

    val dao = HeartRateApp.appDatabase.HeartRateDao()

    suspend fun queryHeartRate(limit: Int = 10, offset: Int) = withContext(Dispatchers.IO) {
        val index = offset * limit
        dao.queryHeartRate(limit, index)
    }

    suspend fun insertHeartRate(heartRate: Int) = withContext(Dispatchers.IO) {
        dao.insertHeartRate(HeartRateEntity(date = LocalDateTime.now(), heartRate = heartRate))
    }
}