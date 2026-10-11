package com.example.platform

import com.example.data.db.DriverFactory

/** Informasi platform yang sedang berjalan (Android / iOS / lainnya). */
expect object Platform {
    val name: String
}

/** UUID acak lintas-platform (pengganti java.util.UUID.randomUUID). */
expect fun randomUuid(): String

/**
 * Membagikan teks (fitur ekspor CSV).
 * - Android : ACTION_SEND chooser
 * - iOS     : UIActivityViewController (share sheet)
 */
expect fun shareText(text: String, title: String)

/** Factory driver SQLite per-platform untuk SQLDelight. */
expect fun createDriverFactory(): DriverFactory
