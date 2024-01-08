package com.atom.heartratecompose.app

import android.app.Application
import android.content.Context
import com.atom.heartratecompose.db.AppDatabase
import kotlin.properties.Delegates

/**
 *  author : liuxe
 *  date : 2024/1/5 16:05
 *  description :
 */
class HeartRateApp : Application() {
    companion object {
        var context: Context by Delegates.notNull()
        var appDatabase: AppDatabase by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        appDatabase = AppDatabase.getDatabase(this)
    }
}