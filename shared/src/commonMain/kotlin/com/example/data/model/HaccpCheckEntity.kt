package com.example.data.model

import kotlinx.datetime.Clock
// Table: haccp_checks (didefinisikan dalam SQLDelight .sq)
data class HaccpCheckEntity(
    
    val id: Int = 1,
    val coldStorageOk: Boolean = false,
    val organolepticOk: Boolean = false,
    val sanitationOk: Boolean = false,
    val labelOk: Boolean = false,
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds()
)
