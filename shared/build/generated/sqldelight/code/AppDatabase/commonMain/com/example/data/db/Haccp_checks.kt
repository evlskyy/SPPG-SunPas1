package com.example.`data`.db

import kotlin.Long

public data class Haccp_checks(
  public val id: Long,
  public val coldStorageOk: Long,
  public val organolepticOk: Long,
  public val sanitationOk: Long,
  public val labelOk: Long,
  public val updatedAt: Long,
)
