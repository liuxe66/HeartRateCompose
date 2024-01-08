package com.atom.heartratecompose.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.atom.heartratecompose.db.dao.HeartRateDao
import com.atom.heartratecompose.db.entity.HeartRateEntity

/**
 *  author : liuxe
 *  date : 2023/3/22 16:13
 *  description :
 */
@Database(
    entities = [HeartRateEntity::class], version = 1, exportSchema = false
)
@TypeConverters(value = [LocalTypeConverter::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun HeartRateDao(): HeartRateDao

    companion object {

        @JvmStatic
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "heart_rate.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}