package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "extraction_records")
data class ExtractionRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sourceSnippet: String,
    val tasksExtracted: Int,
    val menusExtracted: Int,
    val timestamp: Long = System.currentTimeMillis()
)
