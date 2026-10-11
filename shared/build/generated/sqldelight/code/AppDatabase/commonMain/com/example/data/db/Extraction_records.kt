package com.example.`data`.db

import kotlin.Long
import kotlin.String

public data class Extraction_records(
  public val id: Long,
  public val sourceSnippet: String,
  public val tasksExtracted: Long,
  public val menusExtracted: Long,
  public val timestamp: Long,
)
