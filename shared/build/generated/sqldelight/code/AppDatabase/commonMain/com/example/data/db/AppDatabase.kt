package com.example.`data`.db

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.example.`data`.db.shared.newInstance
import com.example.`data`.db.shared.schema
import kotlin.Unit

public interface AppDatabase : Transacter {
  public val dailyMenuQueries: DailyMenuQueries

  public val extractionRecordQueries: ExtractionRecordQueries

  public val haccpCheckQueries: HaccpCheckQueries

  public val kitchenTaskQueries: KitchenTaskQueries

  public companion object {
    public val Schema: SqlSchema<QueryResult.Value<Unit>>
      get() = AppDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): AppDatabase =
        AppDatabase::class.newInstance(driver)
  }
}
