package com.example.`data`.db

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class KitchenTaskQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> getAllTasks(mapper: (
    id: String,
    title: String,
    day: String,
    category: String,
    shift: String,
    assignee: String,
    targetTime: String,
    priority: String,
    status: String,
    sortOrder: Long,
    createdAt: Long,
  ) -> T): Query<T> = Query(140_409_077, arrayOf("kitchen_tasks"), driver, "KitchenTask.sq",
      "getAllTasks",
      "SELECT kitchen_tasks.id, kitchen_tasks.title, kitchen_tasks.day, kitchen_tasks.category, kitchen_tasks.shift, kitchen_tasks.assignee, kitchen_tasks.targetTime, kitchen_tasks.priority, kitchen_tasks.status, kitchen_tasks.sortOrder, kitchen_tasks.createdAt FROM kitchen_tasks ORDER BY sortOrder ASC, createdAt ASC") {
      cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getString(6)!!,
      cursor.getString(7)!!,
      cursor.getString(8)!!,
      cursor.getLong(9)!!,
      cursor.getLong(10)!!
    )
  }

  public fun getAllTasks(): Query<Kitchen_tasks> = getAllTasks { id, title, day, category, shift,
      assignee, targetTime, priority, status, sortOrder, createdAt ->
    Kitchen_tasks(
      id,
      title,
      day,
      category,
      shift,
      assignee,
      targetTime,
      priority,
      status,
      sortOrder,
      createdAt
    )
  }

  public fun getTaskCount(): Query<Long> = Query(632_986_882, arrayOf("kitchen_tasks"), driver,
      "KitchenTask.sq", "getTaskCount", "SELECT COUNT(*) FROM kitchen_tasks") { cursor ->
    cursor.getLong(0)!!
  }

  /**
   * @return The number of rows updated.
   */
  public fun upsertTask(
    id: String,
    title: String,
    day: String,
    category: String,
    shift: String,
    assignee: String,
    targetTime: String,
    priority: String,
    status: String,
    sortOrder: Long,
    createdAt: Long,
  ): QueryResult<Long> {
    val result = driver.execute(-139_371_518, """
        |INSERT OR REPLACE INTO kitchen_tasks(id, title, day, category, shift, assignee, targetTime, priority, status, sortOrder, createdAt)
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 11) {
          bindString(0, id)
          bindString(1, title)
          bindString(2, day)
          bindString(3, category)
          bindString(4, shift)
          bindString(5, assignee)
          bindString(6, targetTime)
          bindString(7, priority)
          bindString(8, status)
          bindLong(9, sortOrder)
          bindLong(10, createdAt)
        }
    notifyQueries(-139_371_518) { emit ->
      emit("kitchen_tasks")
    }
    return result
  }

  /**
   * @return The number of rows updated.
   */
  public fun insertTasks(kitchen_tasks: Kitchen_tasks): QueryResult<Long> {
    val result = driver.execute(456_057_191, """
        |INSERT OR REPLACE INTO kitchen_tasks(id, title, day, category, shift, assignee, targetTime, priority, status, sortOrder, createdAt)
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 11) {
          bindString(0, kitchen_tasks.id)
          bindString(1, kitchen_tasks.title)
          bindString(2, kitchen_tasks.day)
          bindString(3, kitchen_tasks.category)
          bindString(4, kitchen_tasks.shift)
          bindString(5, kitchen_tasks.assignee)
          bindString(6, kitchen_tasks.targetTime)
          bindString(7, kitchen_tasks.priority)
          bindString(8, kitchen_tasks.status)
          bindLong(9, kitchen_tasks.sortOrder)
          bindLong(10, kitchen_tasks.createdAt)
        }
    notifyQueries(456_057_191) { emit ->
      emit("kitchen_tasks")
    }
    return result
  }

  /**
   * @return The number of rows updated.
   */
  public fun updateTaskStatus(status: String, id: String): QueryResult<Long> {
    val result = driver.execute(-902_990_418,
        """UPDATE kitchen_tasks SET status = ? WHERE id = ?""", 2) {
          bindString(0, status)
          bindString(1, id)
        }
    notifyQueries(-902_990_418) { emit ->
      emit("kitchen_tasks")
    }
    return result
  }

  /**
   * @return The number of rows updated.
   */
  public fun deleteTaskById(id: String): QueryResult<Long> {
    val result = driver.execute(-1_959_228_560, """DELETE FROM kitchen_tasks WHERE id = ?""", 1) {
          bindString(0, id)
        }
    notifyQueries(-1_959_228_560) { emit ->
      emit("kitchen_tasks")
    }
    return result
  }

  /**
   * @return The number of rows updated.
   */
  public fun deleteAllTasks(): QueryResult<Long> {
    val result = driver.execute(238_993_126, """DELETE FROM kitchen_tasks""", 0)
    notifyQueries(238_993_126) { emit ->
      emit("kitchen_tasks")
    }
    return result
  }
}
