package com.example.`data`.db

import kotlin.Long
import kotlin.String

public data class Kitchen_tasks(
  public val id: String,
  public val title: String,
  public val day: String,
  public val category: String,
  public val shift: String,
  public val assignee: String,
  public val targetTime: String,
  public val priority: String,
  public val status: String,
  public val sortOrder: Long,
  public val createdAt: Long,
)
