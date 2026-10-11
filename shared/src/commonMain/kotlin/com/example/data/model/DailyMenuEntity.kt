package com.example.data.model

import kotlinx.datetime.Clock
// Table: daily_menus (didefinisikan dalam SQLDelight .sq)
data class DailyMenuEntity(
    
    val day: String, // "Senin", "Selasa", "Rabu", "Kamis", "Jumat"
    val porsi: String = "2.503 Porsi",
    val jamKirim: String = "07:45 WIB | 08:40 WIB | 10:00 WIB",
    val menuList: String = "",
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds()
)
