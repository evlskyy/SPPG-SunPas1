package com.example.`data`.db

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class DailyMenuQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> getAllMenus(mapper: (
    day: String,
    porsi: String,
    jamKirim: String,
    menuList: String,
    updatedAt: Long,
  ) -> T): Query<T> = Query(565_681_900, arrayOf("daily_menus"), driver, "DailyMenu.sq",
      "getAllMenus", """
  |SELECT daily_menus.day, daily_menus.porsi, daily_menus.jamKirim, daily_menus.menuList, daily_menus.updatedAt FROM daily_menus
  |ORDER BY CASE day
  |    WHEN 'Senin' THEN 1 WHEN 'Selasa' THEN 2 WHEN 'Rabu' THEN 3
  |    WHEN 'Kamis' THEN 4 WHEN 'Jumat' THEN 5 ELSE 6 END
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getLong(4)!!
    )
  }

  public fun getAllMenus(): Query<Daily_menus> = getAllMenus { day, porsi, jamKirim, menuList,
      updatedAt ->
    Daily_menus(
      day,
      porsi,
      jamKirim,
      menuList,
      updatedAt
    )
  }

  public fun <T : Any> getMenuByDay(day: String, mapper: (
    day: String,
    porsi: String,
    jamKirim: String,
    menuList: String,
    updatedAt: Long,
  ) -> T): Query<T> = GetMenuByDayQuery(day) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getLong(4)!!
    )
  }

  public fun getMenuByDay(day: String): Query<Daily_menus> = getMenuByDay(day) { day_, porsi,
      jamKirim, menuList, updatedAt ->
    Daily_menus(
      day_,
      porsi,
      jamKirim,
      menuList,
      updatedAt
    )
  }

  public fun getMenuCount(): Query<Long> = Query(-579_281_673, arrayOf("daily_menus"), driver,
      "DailyMenu.sq", "getMenuCount", "SELECT COUNT(*) FROM daily_menus") { cursor ->
    cursor.getLong(0)!!
  }

  /**
   * @return The number of rows updated.
   */
  public fun upsertMenu(
    day: String,
    porsi: String,
    jamKirim: String,
    menuList: String,
    updatedAt: Long,
  ): QueryResult<Long> {
    val result = driver.execute(-1_372_579_029, """
        |INSERT OR REPLACE INTO daily_menus(day, porsi, jamKirim, menuList, updatedAt)
        |VALUES (?, ?, ?, ?, ?)
        """.trimMargin(), 5) {
          bindString(0, day)
          bindString(1, porsi)
          bindString(2, jamKirim)
          bindString(3, menuList)
          bindLong(4, updatedAt)
        }
    notifyQueries(-1_372_579_029) { emit ->
      emit("daily_menus")
    }
    return result
  }

  /**
   * @return The number of rows updated.
   */
  public fun insertMenus(daily_menus: Daily_menus): QueryResult<Long> {
    val result = driver.execute(881_330_014, """
        |INSERT OR REPLACE INTO daily_menus(day, porsi, jamKirim, menuList, updatedAt)
        |VALUES (?, ?, ?, ?, ?)
        """.trimMargin(), 5) {
          bindString(0, daily_menus.day)
          bindString(1, daily_menus.porsi)
          bindString(2, daily_menus.jamKirim)
          bindString(3, daily_menus.menuList)
          bindLong(4, daily_menus.updatedAt)
        }
    notifyQueries(881_330_014) { emit ->
      emit("daily_menus")
    }
    return result
  }

  private inner class GetMenuByDayQuery<out T : Any>(
    public val day: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("daily_menus", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("daily_menus", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-579_954_771,
        """SELECT daily_menus.day, daily_menus.porsi, daily_menus.jamKirim, daily_menus.menuList, daily_menus.updatedAt FROM daily_menus WHERE day = ? LIMIT 1""",
        mapper, 1) {
      bindString(0, day)
    }

    override fun toString(): String = "DailyMenu.sq:getMenuByDay"
  }
}
