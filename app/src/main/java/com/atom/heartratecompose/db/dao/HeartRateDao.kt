package com.atom.heartratecompose.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.atom.heartratecompose.db.entity.HeartRateEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 *  author : liuxe
 *  date : 2024/1/5 17:01
 *  description :
 */
@Dao
interface HeartRateDao {

    @Query("SELECT * FROM heart_rate ORDER BY date DESC LIMIT :limit OFFSET :offset")
    suspend fun queryHeartRate(limit: Int, offset: Int): List<HeartRateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeartRate(heartRate: HeartRateEntity)

}