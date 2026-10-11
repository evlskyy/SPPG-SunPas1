package com.example.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.data.db.AppDatabase
import com.example.data.db.KitchenTask
import com.example.data.model.DailyMenuEntity
import com.example.data.model.ExtractionRecordEntity
import com.example.data.model.HaccpCheckEntity
import com.example.data.model.KitchenTaskEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Pengganti KitchenDao (Room) berbasis SQLDelight — berjalan identik di Android & iOS.
 */
class KitchenDao(private val database: AppDatabase, private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default) {

    // ==== Mapping SQLDelight row -> Entity ====
    private fun KitchenTask.toEntity() = KitchenTaskEntity(
        id = id, title = title, day = day, category = category, shift = shift,
        assignee = assignee, targetTime = targetTime, priority = priority,
        status = status, sortOrder = sortOrder, createdAt = createdAt
    )

    private fun KitchenTaskEntity.toRow() = KitchenTask(
        id = id, title = title, day = day, category = category, shift = shift,
        assignee = assignee, targetTime = targetTime, priority = priority,
        status = status, sortOrder = sortOrder, createdAt = createdAt
    )

    private fun com.example.data.db.DailyMenu.toEntity() = DailyMenuEntity(
        day = day, porsi = porsi, jamKirim = jamKirim, menuList = menuList, updatedAt = updatedAt
    )

    private fun DailyMenuEntity.toRow() = com.example.data.db.DailyMenu(
        day = day, porsi = porsi, jamKirim = jamKirim, menuList = menuList, updatedAt = updatedAt
    )

    private fun com.example.data.db.HaccpCheck.toEntity() = HaccpCheckEntity(
        id = id, coldStorageOk = coldStorageOk, organolepticOk = organolepticOk,
        sanitationOk = sanitationOk, labelOk = labelOk, updatedAt = updatedAt
    )

    private fun HaccpCheckEntity.toRow() = com.example.data.db.HaccpCheck(
        id = id, coldStorageOk = coldStorageOk, organolepticOk = organolepticOk,
        sanitationOk = sanitationOk, labelOk = labelOk, updatedAt = updatedAt
    )

    private fun com.example.data.db.ExtractionRecord.toEntity() = ExtractionRecordEntity(
        id = id, sourceSnippet = sourceSnippet, tasksExtracted = tasksExtracted,
        menusExtracted = menusExtracted, timestamp = timestamp
    )

    // ==== Tasks ====
    fun getAllTasks(): Flow<List<KitchenTaskEntity>> =
        database.kitchenTaskQueries.getAllTasks()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { list -> list.map { it.toEntity() } }

    suspend fun getTaskCount(): Int = withContext(ioDispatcher) {
        database.kitchenTaskQueries.getTaskCount().executeAsOne().toInt()
    }

    suspend fun insertTask(task: KitchenTaskEntity) = withContext(ioDispatcher) {
        database.kitchenTaskQueries.upsertTask(task.toRow())
    }

    suspend fun insertTasks(tasks: List<KitchenTaskEntity>) = withContext(ioDispatcher) {
        database.transaction {
            tasks.forEach { database.kitchenTaskQueries.upsertTask(it.toRow()) }
        }
    }

    suspend fun updateTask(task: KitchenTaskEntity) = insertTask(task)

    suspend fun updateTaskStatus(id: String, status: String) = withContext(ioDispatcher) {
        database.kitchenTaskQueries.updateTaskStatus(status = status, id = id)
    }

    suspend fun deleteTaskById(id: String) = withContext(ioDispatcher) {
        database.kitchenTaskQueries.deleteTaskById(id)
    }

    suspend fun deleteAllTasks() = withContext(ioDispatcher) {
        database.kitchenTaskQueries.deleteAllTasks()
    }

    // ==== Daily Menus ====
    fun getAllMenus(): Flow<List<DailyMenuEntity>> =
        database.dailyMenuQueries.getAllMenus()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { list -> list.map { it.toEntity() } }

    fun getMenuByDay(day: String): Flow<DailyMenuEntity?> =
        database.dailyMenuQueries.getMenuByDay(day)
            .asFlow()
            .mapToOneOrNull(ioDispatcher)
            .map { it?.toEntity() }

    suspend fun insertMenus(menus: List<DailyMenuEntity>) = withContext(ioDispatcher) {
        database.transaction {
            menus.forEach { database.dailyMenuQueries.upsertMenu(it.toRow()) }
        }
    }

    suspend fun upsertMenu(menu: DailyMenuEntity) = withContext(ioDispatcher) {
        database.dailyMenuQueries.upsertMenu(menu.toRow())
    }

    suspend fun getMenuCount(): Int = withContext(ioDispatcher) {
        database.dailyMenuQueries.getMenuCount().executeAsOne().toInt()
    }

    // ==== HACCP ====
    fun getHaccpCheck(): Flow<HaccpCheckEntity?> =
        database.haccpCheckQueries.getHaccpCheck()
            .asFlow()
            .mapToOneOrNull(ioDispatcher)
            .map { it?.toEntity() }

    suspend fun upsertHaccpCheck(haccp: HaccpCheckEntity) = withContext(ioDispatcher) {
        database.haccpCheckQueries.upsertHaccpCheck(haccp.toRow())
    }

    // ==== Extraction Records ====
    fun getAllExtractions(): Flow<List<ExtractionRecordEntity>> =
        database.extractionRecordQueries.getAllExtractions()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { list -> list.map { it.toEntity() } }

    suspend fun insertExtraction(record: ExtractionRecordEntity) = withContext(ioDispatcher) {
        database.extractionRecordQueries.insertExtraction(
            sourceSnippet = record.sourceSnippet,
            tasksExtracted = record.tasksExtracted,
            menusExtracted = record.menusExtracted,
            timestamp = record.timestamp
        )
    }
}
