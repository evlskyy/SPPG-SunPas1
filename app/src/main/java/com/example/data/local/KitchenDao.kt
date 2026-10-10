package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DailyMenuEntity
import com.example.data.model.ExtractionRecordEntity
import com.example.data.model.HaccpCheckEntity
import com.example.data.model.KitchenTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KitchenDao {

    // Tasks
    @Query("SELECT * FROM kitchen_tasks ORDER BY sortOrder ASC, createdAt ASC")
    fun getAllTasks(): Flow<List<KitchenTaskEntity>>

    @Query("SELECT COUNT(*) FROM kitchen_tasks")
    suspend fun getTaskCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: KitchenTaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<KitchenTaskEntity>)

    @Update
    suspend fun updateTask(task: KitchenTaskEntity)

    @Query("UPDATE kitchen_tasks SET status = :status WHERE id = :id")
    suspend fun updateTaskStatus(id: String, status: String)

    @Query("DELETE FROM kitchen_tasks WHERE id = :id")
    suspend fun deleteTaskById(id: String)

    @Delete
    suspend fun deleteTask(task: KitchenTaskEntity)

    @Query("DELETE FROM kitchen_tasks")
    suspend fun deleteAllTasks()

    // Daily Menus
    @Query("SELECT * FROM daily_menus ORDER BY CASE day WHEN 'Senin' THEN 1 WHEN 'Selasa' THEN 2 WHEN 'Rabu' THEN 3 WHEN 'Kamis' THEN 4 WHEN 'Jumat' THEN 5 ELSE 6 END")
    fun getAllMenus(): Flow<List<DailyMenuEntity>>

    @Query("SELECT * FROM daily_menus WHERE day = :day LIMIT 1")
    fun getMenuByDay(day: String): Flow<DailyMenuEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenus(menus: List<DailyMenuEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMenu(menu: DailyMenuEntity)

    @Query("SELECT COUNT(*) FROM daily_menus")
    suspend fun getMenuCount(): Int

    // HACCP
    @Query("SELECT * FROM haccp_checks WHERE id = 1 LIMIT 1")
    fun getHaccpCheck(): Flow<HaccpCheckEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHaccpCheck(haccp: HaccpCheckEntity)

    // Extraction Records
    @Query("SELECT * FROM extraction_records ORDER BY timestamp DESC")
    fun getAllExtractions(): Flow<List<ExtractionRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExtraction(record: ExtractionRecordEntity)
}
