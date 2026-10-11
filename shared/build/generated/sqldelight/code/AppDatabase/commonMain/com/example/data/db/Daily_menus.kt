package com.example.`data`.db

import kotlin.Long
import kotlin.String

public data class Daily_menus(
  public val day: String,
  public val porsi: String,
  public val jamKirim: String,
  public val menuList: String,
  public val updatedAt: Long,
)
