package com.example.data.db

import app.cash.sqldelight.db.SqlDriver

/**
 * Abstraksi lintas-platform untuk pembuatan driver SQLite (SQLDelight).
 * Implementasi per-platform tersedia di androidMain & iosMain.
 */
interface DriverFactory {
    fun createDriver(): SqlDriver
}

/** Membangun instance [AppDatabase] dari sebuah [DriverFactory]. */
fun createAppDatabase(driverFactory: DriverFactory): AppDatabase {
    val driver = driverFactory.createDriver()
    return AppDatabase(
        driver = driver,
        kitchenTaskQueries = KitchenTaskQueries(driver),
        dailyMenuQueries = DailyMenuQueries(driver),
        haccpCheckQueries = HaccpCheckQueries(driver),
        extractionRecordQueries = ExtractionRecordQueries(driver)
    )
}
