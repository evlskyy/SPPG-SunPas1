package com.example.`data`.db.shared

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.example.`data`.db.AppDatabase
import com.example.`data`.db.DailyMenuQueries
import com.example.`data`.db.ExtractionRecordQueries
import com.example.`data`.db.HaccpCheckQueries
import com.example.`data`.db.KitchenTaskQueries
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass

internal val KClass<AppDatabase>.schema: SqlSchema<QueryResult.Value<Unit>>
  get() = AppDatabaseImpl.Schema

internal fun KClass<AppDatabase>.newInstance(driver: SqlDriver): AppDatabase =
    AppDatabaseImpl(driver)

private class AppDatabaseImpl(
  driver: SqlDriver,
) : TransacterImpl(driver),
    AppDatabase {
  override val dailyMenuQueries: DailyMenuQueries = DailyMenuQueries(driver)

  override val extractionRecordQueries: ExtractionRecordQueries = ExtractionRecordQueries(driver)

  override val haccpCheckQueries: HaccpCheckQueries = HaccpCheckQueries(driver)

  override val kitchenTaskQueries: KitchenTaskQueries = KitchenTaskQueries(driver)

  public object Schema : SqlSchema<QueryResult.Value<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
      driver.execute(null, """
          |CREATE TABLE daily_menus (
          |    day TEXT NOT NULL PRIMARY KEY,
          |    porsi TEXT NOT NULL DEFAULT '2.503 Porsi',
          |    jamKirim TEXT NOT NULL DEFAULT '07:45 WIB | 08:40 WIB | 10:00 WIB',
          |    menuList TEXT NOT NULL DEFAULT '',
          |    updatedAt INTEGER NOT NULL DEFAULT 0
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE extraction_records (
          |    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |    sourceSnippet TEXT NOT NULL,
          |    tasksExtracted INTEGER NOT NULL,
          |    menusExtracted INTEGER NOT NULL,
          |    timestamp INTEGER NOT NULL DEFAULT 0
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE haccp_checks (
          |    id INTEGER NOT NULL PRIMARY KEY,
          |    coldStorageOk INTEGER NOT NULL DEFAULT 0,
          |    organolepticOk INTEGER NOT NULL DEFAULT 0,
          |    sanitationOk INTEGER NOT NULL DEFAULT 0,
          |    labelOk INTEGER NOT NULL DEFAULT 0,
          |    updatedAt INTEGER NOT NULL DEFAULT 0
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE kitchen_tasks (
          |    id TEXT NOT NULL PRIMARY KEY,
          |    title TEXT NOT NULL,
          |    day TEXT NOT NULL DEFAULT 'Semua Hari',
          |    category TEXT NOT NULL DEFAULT 'Persiapan Bahan',
          |    shift TEXT NOT NULL DEFAULT 'Shift 1 (Malam/Subuh)',
          |    assignee TEXT NOT NULL DEFAULT 'Tim Dapur',
          |    targetTime TEXT NOT NULL DEFAULT '06:00 WIB',
          |    priority TEXT NOT NULL DEFAULT 'Sedang',
          |    status TEXT NOT NULL DEFAULT 'Belum Dimulai',
          |    sortOrder INTEGER NOT NULL DEFAULT 0,
          |    createdAt INTEGER NOT NULL DEFAULT 0
          |)
          """.trimMargin(), 0)
      return QueryResult.Unit
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> = QueryResult.Unit
  }
}
