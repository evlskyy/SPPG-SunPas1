package com.example.platform

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.data.db.AppDatabase
import com.example.data.db.DriverFactory

actual object Platform {
    actual val name: String = "Android"
}

private var appContext: Context? = null

/** Harus dipanggil sekali dari MainActivity (Android). */
fun initAndroidPlatform(context: Context) {
    appContext = context.applicationContext
}

actual fun randomUuid(): String = java.util.UUID.randomUUID().toString()

actual fun shareText(text: String, title: String) {
    val ctx = appContext ?: return
    val sendIntent = android.content.Intent().apply {
        action = android.content.Intent.ACTION_SEND
        putExtra(android.content.Intent.EXTRA_TEXT, text)
        type = "text/csv"
    }
    val chooser = android.content.Intent.createChooser(sendIntent, title)
    chooser.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
    ctx.startActivity(chooser)
}

actual fun createDriverFactory(): DriverFactory = object : DriverFactory {
    override fun createDriver(): SqlDriver =
        AndroidSqliteDriver(AppDatabase.Schema, appContext!!, "dapur_mbg.db")
}
