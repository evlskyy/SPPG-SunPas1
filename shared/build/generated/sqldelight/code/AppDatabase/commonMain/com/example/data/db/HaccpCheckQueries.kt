package com.example.`data`.db

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long

public class HaccpCheckQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> getHaccpCheck(mapper: (
    id: Long,
    coldStorageOk: Long,
    organolepticOk: Long,
    sanitationOk: Long,
    labelOk: Long,
    updatedAt: Long,
  ) -> T): Query<T> = Query(-1_446_797_729, arrayOf("haccp_checks"), driver, "HaccpCheck.sq",
      "getHaccpCheck",
      "SELECT haccp_checks.id, haccp_checks.coldStorageOk, haccp_checks.organolepticOk, haccp_checks.sanitationOk, haccp_checks.labelOk, haccp_checks.updatedAt FROM haccp_checks WHERE id = 1 LIMIT 1") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4)!!,
      cursor.getLong(5)!!
    )
  }

  public fun getHaccpCheck(): Query<Haccp_checks> = getHaccpCheck { id, coldStorageOk,
      organolepticOk, sanitationOk, labelOk, updatedAt ->
    Haccp_checks(
      id,
      coldStorageOk,
      organolepticOk,
      sanitationOk,
      labelOk,
      updatedAt
    )
  }

  /**
   * @return The number of rows updated.
   */
  public fun upsertHaccpCheck(
    id: Long?,
    coldStorageOk: Long,
    organolepticOk: Long,
    sanitationOk: Long,
    labelOk: Long,
    updatedAt: Long,
  ): QueryResult<Long> {
    val result = driver.execute(-274_735_416, """
        |INSERT OR REPLACE INTO haccp_checks(id, coldStorageOk, organolepticOk, sanitationOk, labelOk, updatedAt)
        |VALUES (?, ?, ?, ?, ?, ?)
        """.trimMargin(), 6) {
          bindLong(0, id)
          bindLong(1, coldStorageOk)
          bindLong(2, organolepticOk)
          bindLong(3, sanitationOk)
          bindLong(4, labelOk)
          bindLong(5, updatedAt)
        }
    notifyQueries(-274_735_416) { emit ->
      emit("haccp_checks")
    }
    return result
  }
}
