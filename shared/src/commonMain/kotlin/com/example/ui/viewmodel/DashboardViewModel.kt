package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.extraction.AutoTextExtractor
import com.example.data.extraction.ExtractionResult
import com.example.data.model.DailyMenuEntity
import com.example.data.model.HaccpCheckEntity
import com.example.data.model.KitchenTaskEntity
import com.example.data.repository.KitchenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.platform.randomUuid
import kotlinx.datetime.Clock

data class DashboardUiState(
    val allTasks: List<KitchenTaskEntity> = emptyList(),
    val allMenus: List<DailyMenuEntity> = emptyList(),
    val haccpCheck: HaccpCheckEntity = HaccpCheckEntity(),
    val searchQuery: String = "",
    val selectedCategory: String = "all",
    val selectedDay: String = "all",
    val selectedShift: String = "all",
    val selectedStatus: String = "all",
    val selectedMenuDay: String = "Senin",
    val showTaskDialog: Boolean = false,
    val editingTask: KitchenTaskEntity? = null,
    val showMenuDialog: Boolean = false,
    val showScheduleDialog: Boolean = false,
    val showLogoDialog: Boolean = false,
    val logoEmoji: String = "🍴",
    val showExtractDialog: Boolean = false,
    val extractionPreview: ExtractionResult? = null,
    val isExtracting: Boolean = false,
    val toastMessage: String? = null
) {
    val filteredTasks: List<KitchenTaskEntity>
        get() = allTasks.filter { task ->
            val matchCategory = selectedCategory == "all" || task.category == selectedCategory
            val matchDay = selectedDay == "all" || task.day == selectedDay || task.day == "Semua Hari"
            val matchShift = selectedShift == "all" || task.shift == selectedShift
            val matchStatus = selectedStatus == "all" || task.status == selectedStatus
            val query = searchQuery.trim().lowercase()
            val matchSearch = query.isEmpty() ||
                    task.title.lowercase().contains(query) ||
                    task.assignee.lowercase().contains(query) ||
                    task.day.lowercase().contains(query) ||
                    task.category.lowercase().contains(query)
            matchCategory && matchDay && matchShift && matchStatus && matchSearch
        }

    val totalCount: Int get() = filteredTasks.size
    val completedCount: Int get() = filteredTasks.count { it.status == "Selesai" }
    val inProgressCount: Int get() = filteredTasks.count { it.status == "Dalam Proses" }
    val pendingCount: Int get() = filteredTasks.count { it.status == "Belum Dimulai" }

    val progressPercent: Int
        get() = if (totalCount > 0) ((completedCount.toDouble() / totalCount.toDouble()) * 100).toInt() else 0

    val isReadyForDistribution: Boolean
        get() = totalCount > 0 && progressPercent == 100

    val currentMenu: DailyMenuEntity?
        get() = allMenus.firstOrNull { it.day.equals(selectedMenuDay, ignoreCase = true) }
            ?: allMenus.firstOrNull()
}

class DashboardViewModel(private val repository: KitchenRepository) : ViewModel() {

    companion object {
        /** Factory lintas-platform (dipakai Android & iOS). */
        fun factory(driverFactory: com.example.data.db.DriverFactory): DashboardViewModel {
            val database = com.example.data.db.createAppDatabase(driverFactory)
            val dao = com.example.data.local.KitchenDao(database)
            val repository = KitchenRepository(dao, ioDispatcher = kotlinx.coroutines.Dispatchers.Default)
            return DashboardViewModel(repository)
        }
    }


    init {
        viewModelScope.launch {
            repository.initializeDatabaseIfEmpty()
        }
    }

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow("all")
    private val _selectedDay = MutableStateFlow("all")
    private val _selectedShift = MutableStateFlow("all")
    private val _selectedStatus = MutableStateFlow("all")
    private val _selectedMenuDay = MutableStateFlow("Senin")

    private val _showTaskDialog = MutableStateFlow(false)
    private val _editingTask = MutableStateFlow<KitchenTaskEntity?>(null)
    private val _showMenuDialog = MutableStateFlow(false)
    private val _showScheduleDialog = MutableStateFlow(false)
    private val _showLogoDialog = MutableStateFlow(false)
    private val _logoEmoji = MutableStateFlow("🍴")
    private val _showExtractDialog = MutableStateFlow(false)
    private val _extractionPreview = MutableStateFlow<ExtractionResult?>(null)
    private val _isExtracting = MutableStateFlow(false)
    private val _toastMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.allTasks,
        repository.allMenus,
        repository.haccpCheck,
        combine(
            _searchQuery,
            _selectedCategory,
            _selectedDay,
            _selectedShift,
            _selectedStatus
        ) { search, cat, day, shift, status ->
            FilterTuple(search, cat, day, shift, status)
        },
        combine(
            _selectedMenuDay,
            _showTaskDialog,
            _editingTask,
            _showMenuDialog,
            _showExtractDialog,
            _showScheduleDialog,
            _showLogoDialog,
            _logoEmoji
        ) { args ->
            DialogTuple(
                menuDay = args[0] as String,
                taskDlg = args[1] as Boolean,
                editTask = args[2] as KitchenTaskEntity?,
                menuDlg = args[3] as Boolean,
                extDlg = args[4] as Boolean,
                scheduleDlg = args[5] as Boolean,
                logoDlg = args[6] as Boolean,
                logoEmoji = args[7] as String
            )
        }
    ) { tasks, menus, haccp, filters, dialogs ->
        DashboardUiState(
            allTasks = tasks,
            allMenus = menus,
            haccpCheck = haccp ?: HaccpCheckEntity(),
            searchQuery = filters.search,
            selectedCategory = filters.category,
            selectedDay = filters.day,
            selectedShift = filters.shift,
            selectedStatus = filters.status,
            selectedMenuDay = dialogs.menuDay,
            showTaskDialog = dialogs.taskDlg,
            editingTask = dialogs.editTask,
            showMenuDialog = dialogs.menuDlg,
            showScheduleDialog = dialogs.scheduleDlg,
            showLogoDialog = dialogs.logoDlg,
            logoEmoji = dialogs.logoEmoji,
            showExtractDialog = dialogs.extDlg,
            extractionPreview = _extractionPreview.value,
            isExtracting = _isExtracting.value,
            toastMessage = _toastMessage.value
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategoryFilter(category: String) {
        _selectedCategory.value = category
    }

    fun setDayFilter(day: String) {
        _selectedDay.value = day
        if (day != "all" && listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat").contains(day)) {
            _selectedMenuDay.value = day
        }
    }

    fun setShiftFilter(shift: String) {
        _selectedShift.value = shift
    }

    fun setStatusFilter(status: String) {
        _selectedStatus.value = status
    }

    fun setSelectedMenuDay(day: String) {
        _selectedMenuDay.value = day
    }

    fun toggleTaskDone(task: KitchenTaskEntity) {
        viewModelScope.launch {
            val newStatus = if (task.status == "Selesai") "Belum Dimulai" else "Selesai"
            repository.updateTaskStatus(task.id, newStatus)
            showToast("Status tugas: $newStatus")
        }
    }

    fun updateTaskStatus(taskId: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateTaskStatus(taskId, newStatus)
            showToast("Status tugas diubah ke $newStatus")
        }
    }

    fun openAddTask() {
        _editingTask.value = null
        _showTaskDialog.value = true
    }

    fun openEditTask(task: KitchenTaskEntity) {
        _editingTask.value = task
        _showTaskDialog.value = true
    }

    fun closeTaskDialog() {
        _showTaskDialog.value = false
        _editingTask.value = null
    }

    fun saveTask(
        title: String,
        day: String,
        category: String,
        shift: String,
        assignee: String,
        targetTime: String,
        priority: String,
        status: String
    ) {
        viewModelScope.launch {
            val existing = _editingTask.value
            if (existing != null) {
                val updated = existing.copy(
                    title = title,
                    day = day,
                    category = category,
                    shift = shift,
                    assignee = assignee,
                    targetTime = targetTime,
                    priority = priority,
                    status = status
                )
                repository.updateTask(updated)
                showToast("Tugas berhasil diperbarui")
            } else {
                val newTask = KitchenTaskEntity(
                    id = randomUuid().take(8),
                    title = title,
                    day = day,
                    category = category,
                    shift = shift,
                    assignee = assignee,
                    targetTime = targetTime,
                    priority = priority,
                    status = status,
                    sortOrder = uiState.value.allTasks.size + 1,
                    createdAt = Clock.System.now().toEpochMilliseconds()
                )
                repository.insertTask(newTask)
                showToast("Tugas baru berhasil disimpan ke database lokal")
            }
            closeTaskDialog()
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
            showToast("Tugas berhasil dihapus dari database")
        }
    }

    fun markAllVisibleAsDone() {
        val visibleIds = uiState.value.filteredTasks.map { it.id }
        if (visibleIds.isEmpty()) return
        viewModelScope.launch {
            repository.markTasksAsDone(visibleIds)
            showToast("${visibleIds.size} tugas berhasil ditandai selesai")
        }
    }

    fun resetToDefaultData() {
        viewModelScope.launch {
            repository.resetToDefaultData()
            showToast("Data joblist & menu direset ke standar dokumen bawaan")
        }
    }

    fun openMenuDialog() {
        _showMenuDialog.value = true
    }

    // ==== Jadwal Distribusi (edit cepat, berlaku untuk semua hari) ====
    fun openScheduleDialog() {
        _showScheduleDialog.value = true
    }

    fun closeScheduleDialog() {
        _showScheduleDialog.value = false
    }

    fun saveSchedule(jamKirim: String) {
        val trimmed = jamKirim.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch {
            repository.updateJamDistribusiAllDays(trimmed)
            showToast("Jadwal distribusi diperbarui: $trimmed")
            closeScheduleDialog()
        }
    }

    // ==== Kustomisasi Logo Garpu & Sendok ====
    fun openLogoDialog() {
        _showLogoDialog.value = true
    }

    fun closeLogoDialog() {
        _showLogoDialog.value = false
    }

    fun saveLogoEmoji(emoji: String) {
        val chosen = emoji.trim().ifEmpty { "🍴" }
        _logoEmoji.value = chosen
        platformSaveString("app_logo_emoji", chosen)
        showToast("Logo aplikasi diganti menjadi $chosen")
        closeLogoDialog()
    }

    fun loadSavedLogo() {
        _logoEmoji.value = platformGetString("app_logo_emoji", "🍴")
    }

    fun closeMenuDialog() {
        _showMenuDialog.value = false
    }

    fun saveMenu(day: String, porsi: String, jamKirim: String, menuList: String) {
        viewModelScope.launch {
            val menu = DailyMenuEntity(
                day = day,
                porsi = porsi,
                jamKirim = jamKirim,
                menuList = menuList,
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
            repository.upsertMenu(menu)
            showToast("Menu hari $day berhasil diperbarui")
            closeMenuDialog()
        }
    }

    fun toggleHaccp(checkType: String, checked: Boolean) {
        viewModelScope.launch {
            val current = uiState.value.haccpCheck
            val updated = when (checkType) {
                "coldStorage" -> current.copy(coldStorageOk = checked)
                "organoleptic" -> current.copy(organolepticOk = checked)
                "sanitation" -> current.copy(sanitationOk = checked)
                "label" -> current.copy(labelOk = checked)
                else -> current
            }
            repository.upsertHaccpCheck(updated)
        }
    }

    // Automatic Text Extraction Engine Controls
    fun openExtractDialog() {
        _extractionPreview.value = null
        _showExtractDialog.value = true
    }

    fun closeExtractDialog() {
        _showExtractDialog.value = false
        _extractionPreview.value = null
    }

    fun previewExtraction(text: String) {
        _isExtracting.value = true
        val result = AutoTextExtractor.extract(text)
        _extractionPreview.value = result
        _isExtracting.value = false
    }

    fun confirmSaveExtractedData(replaceExisting: Boolean = false) {
        val preview = _extractionPreview.value ?: return
        viewModelScope.launch {
            if (replaceExisting) {
                repository.resetToDefaultData()
            }
            repository.extractAndSave(preview.rawTextSnippet.ifEmpty { AutoTextExtractor.ORIGINAL_DOCUMENT_TEXT })
            showToast("Ekstraksi berhasil: ${preview.tasks.size} tugas & ${preview.menus.size} menu disimpan ke database lokal!")
            closeExtractDialog()
        }
    }

    fun exportToCsvContent(): String {
        val tasks = uiState.value.filteredTasks
        val sb = StringBuilder()
        sb.append("ID,Hari,Tugas,Kategori,Shift,PJ,Jam Target,Prioritas,Status\n")
        for (t in tasks) {
            val titleEscaped = "\"" + t.title.replace("\"", "\"\"") + "\""
            val assigneeEscaped = "\"" + t.assignee.replace("\"", "\"\"") + "\""
            sb.append("${t.id},\"${t.day}\",$titleEscaped,\"${t.category}\",\"${t.shift}\",$assigneeEscaped,\"${t.targetTime}\",\"${t.priority}\",\"${t.status}\"\n")
        }
        return sb.toString()
    }

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}

private data class FilterTuple(
    val search: String,
    val category: String,
    val day: String,
    val shift: String,
    val status: String
)

private data class DialogTuple(
    val menuDay: String,
    val taskDlg: Boolean,
    val editTask: KitchenTaskEntity?,
    val menuDlg: Boolean,
    val extDlg: Boolean,
    val scheduleDlg: Boolean,
    val logoDlg: Boolean,
    val logoEmoji: String
)
