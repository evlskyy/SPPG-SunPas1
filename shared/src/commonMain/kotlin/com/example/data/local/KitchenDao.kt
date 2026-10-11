package com.example.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.data.db.AppDatabase
import com.example.data.db.Daily_menus
import com.example.data.db.Extraction_records
import com.example.data.db.Haccp_checks
import com.example.data.db.Kitchen_tasks
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
    private fun Kitchen_tasks.toEntity() = KitchenTaskEntity(
        id = id, title = title, day = day, category = category, shift = shift,
        assignee = assignee, targetTime = targetTime, priority = priority,
        status = status, sortOrder = sortOrder.toInt(), createdAt = createdAt
    )

    private fun KitchenTaskEntity.toRow() = Kitchen_tasks(
        id = id, title = title, day = day, category = category, shift = shift,
        assignee = assignee, targetTime = targetTime, priority = priority,
        status = status, sortOrder = sortOrder.toLong(), createdAt = createdAt
    )

    private fun Daily_menus.toEntity() = DailyMenuEntity(
        day = day, porsi = porsi, jamKirim = jamKirim, menuList = menuList, updatedAt = updatedAt
    )

    private fun Haccp_checks.toEntity() = HaccpCheckEntity(
        id = id.toInt(), coldStorageOk = coldStorageOk != 0L,
        organolepticOk = organolepticOk != 0L, sanitationOk = sanitationOk != 0L,
        labelOk = labelOk != 0L, updatedAt = updatedAt
    )

    private fun Extraction_records.toEntity() = ExtractionRecordEntity(
        id = id, sourceSnippet = sourceSnippet, tasksExtracted = tasksExtracted.toInt(),
        menusExtracted = menusExtracted.toInt(), timestamp = timestamp
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
        database.kitchenTaskQueries.upsertTask(
            id = task.id, title = task.title, day = task.day, category = task.category,
            shift = task.shift, assignee = task.assignee, targetTime = task.targetTime,
            priority = task.priority, status = task.status, sortOrder = task.sortOrder.toLong(),
            createdAt = task.createdAt
        )
    }

    suspend fun insertTasks(tasks: List<KitchenTaskEntity>) = withContext(ioDispatcher) {
        database.transaction {
            tasks.forEach { database.kitchenTaskQueries.insertTasks(it.toRow()) }
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
            menus.forEach { m ->
                database.dailyMenuQueries.upsertMenu(
                    day = m.day, porsi = m.porsi, jamKirim = m.jamKirim,
                    menuList = m.menuList, updatedAt = m.updatedAt
                )
            }
        }
    }

    suspend fun upsertMenu(menu: DailyMenuEntity) = withContext(ioDispatcher) {
        database.dailyMenuQueries.upsertMenu(
            day = menu.day, porsi = menu.porsi, jamKirim = menu.jamKirim,
            menuList = menu.menuList, updatedAt = menu.updatedAt
        )
    }

    /** Update jadwal distribusi (jamKirim) untuk SEMUA hari sekaligus. */
    suspend fun updateJamKirimAllDays(jamKirim: String) = withContext(ioDispatcher) {
        database.dailyMenuQueries.updateJamKirimAllDays(
            jamKirim = jamKirim,
            updatedAt = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
        )
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
        database.haccpCheckQueries.upsertHaccpCheck(
            id = haccp.id.toLong(),
            coldStorageOk = if (haccp.coldStorageOk) 1L else 0L,
            organolepticOk = if (haccp.organolepticOk) 1L else 0L,
            sanitationOk = if (haccp.sanitationOk) 1L else 0L,
            labelOk = if (haccp.labelOk) 1L else 0L,
            updatedAt = haccp.updatedAt
        )
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
            tasksExtracted = record.tasksExtracted.toLong(),
            menusExtracted = record.menusExtracted.toLong(),
            timestamp = record.timestamp
        )
    }
}
