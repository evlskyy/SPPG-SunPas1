package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kitchen_tasks")
data class KitchenTaskEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val day: String = "Semua Hari", // "Semua Hari", "Senin", "Selasa", "Rabu", "Kamis", "Jumat"
    val category: String = "Persiapan Bahan", // "Persiapan Bahan", "Bumbu & Racikan", "Kebersihan & Sanitasi", "Quality & Safety Check", "Packing & Distribusi Internal"
    val shift: String = "Shift 1 (Malam/Subuh)", // "Shift 1 (Malam/Subuh)", "Shift 2 (Pagi/Siang)", "Shift 3 (Sore/Malam)"
    val assignee: String = "Tim Dapur",
    val targetTime: String = "06:00 WIB",
    val priority: String = "Sedang", // "Tinggi", "Sedang", "Rendah"
    val status: String = "Belum Dimulai", // "Belum Dimulai", "Dalam Proses", "Selesai"
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
