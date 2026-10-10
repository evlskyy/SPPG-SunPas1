package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_menus")
data class DailyMenuEntity(
    @PrimaryKey
    val day: String, // "Senin", "Selasa", "Rabu", "Kamis", "Jumat"
    val porsi: String = "2.503 Porsi",
    val jamKirim: String = "07:45 WIB | 08:40 WIB | 10:00 WIB",
    val menuList: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)
