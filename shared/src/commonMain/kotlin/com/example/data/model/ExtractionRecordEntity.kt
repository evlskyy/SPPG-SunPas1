package com.example.data.model

import kotlinx.datetime.Clock
// Table: extraction_records (didefinisikan dalam SQLDelight .sq)
data class ExtractionRecordEntity(
    // id auto-increment (AUTOINCREMENT di skema SQLDelight)
    val id: Long = 0,
    val sourceSnippet: String,
    val tasksExtracted: Int,
    val menusExtracted: Int,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds()
)
