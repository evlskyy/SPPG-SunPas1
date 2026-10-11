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
    return AppDatabase(driver = driver)
}

/**
 * Key-value store sederhana berbasis SQLite (tabel internal `app_kv`) —
 * dipakai untuk preferensi ringan seperti logo emoji kustom, berjalan identik
 * di Android & iOS tanpa dependensi platform tambahan.
 */
class KeyValueStore(private val driver: SqlDriver) {

    init {
        driver.execute(
            identifier = null,
            sql = "CREATE TABLE IF NOT EXISTS app_kv (key TEXT NOT NULL PRIMARY KEY, value TEXT NOT NULL)",
            parameters = 0
        )
    }

    fun getString(key: String, default: String): String {
        val cursor = driver.executeQuery(
            identifier = null,
            sql = "SELECT value FROM app_kv WHERE key = ? LIMIT 1",
            mapper = { c -> if (c.next()) c.getString(0) else null },
            parameters = 1
        ) { bindString(0, key) }
        val result = cursor.use { it.value }
        cursor.close()
        return result ?: default
    }

    fun putString(key: String, value: String) {
        driver.execute(
            identifier = null,
            sql = "INSERT OR REPLACE INTO app_kv(key, value) VALUES (?, ?)",
            parameters = 2
        ) {
            bindString(0, key)
            bindString(1, value)
        }
    }
}
