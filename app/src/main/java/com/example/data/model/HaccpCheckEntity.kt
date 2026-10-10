package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "haccp_checks")
data class HaccpCheckEntity(
    @PrimaryKey
    val id: Int = 1,
    val coldStorageOk: Boolean = false,
    val organolepticOk: Boolean = false,
    val sanitationOk: Boolean = false,
    val labelOk: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
