package com.example.`data`.db

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class ExtractionRecordQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> getAllExtractions(mapper: (
    id: Long,
    sourceSnippet: String,
    tasksExtracted: Long,
    menusExtracted: Long,
    timestamp: Long,
  ) -> T): Query<T> = Query(549_487_218, arrayOf("extraction_records"), driver,
      "ExtractionRecord.sq", "getAllExtractions",
      "SELECT extraction_records.id, extraction_records.sourceSnippet, extraction_records.tasksExtracted, extraction_records.menusExtracted, extraction_records.timestamp FROM extraction_records ORDER BY timestamp DESC") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getLong(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4)!!
    )
  }

  public fun getAllExtractions(): Query<Extraction_records> = getAllExtractions { id, sourceSnippet,
      tasksExtracted, menusExtracted, timestamp ->
    Extraction_records(
      id,
      sourceSnippet,
      tasksExtracted,
      menusExtracted,
      timestamp
    )
  }

  /**
   * @return The number of rows updated.
   */
  public fun insertExtraction(
    sourceSnippet: String,
    tasksExtracted: Long,
    menusExtracted: Long,
    timestamp: Long,
  ): QueryResult<Long> {
    val result = driver.execute(267_440_367, """
        |INSERT OR REPLACE INTO extraction_records(sourceSnippet, tasksExtracted, menusExtracted, timestamp)
        |VALUES (?, ?, ?, ?)
        """.trimMargin(), 4) {
          bindString(0, sourceSnippet)
          bindLong(1, tasksExtracted)
          bindLong(2, menusExtracted)
          bindLong(3, timestamp)
        }
    notifyQueries(267_440_367) { emit ->
      emit("extraction_records")
    }
    return result
  }
}
