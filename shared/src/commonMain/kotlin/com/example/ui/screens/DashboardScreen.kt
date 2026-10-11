package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AutoExtractionDialog
import com.example.ui.components.MenuEditDialog
import com.example.ui.components.TaskCard
import com.example.ui.components.TaskEditDialog
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.Amber950
import com.example.ui.theme.Emerald200
import com.example.ui.theme.Emerald300
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald950
import com.example.ui.theme.Purple300
import com.example.ui.theme.Purple800
import com.example.ui.theme.Purple950
import com.example.ui.theme.Red400
import com.example.ui.theme.Red500
import com.example.ui.theme.Sky400
import com.example.ui.theme.Sky500
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.Teal400
import com.example.ui.theme.Teal500
import com.example.ui.viewmodel.DashboardUiState
import com.example.ui.viewmodel.DashboardViewModel
import kotlinx.coroutines.delay
import com.example.platform.shareText
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Live Clock State (WIB)
    var currentTimeStr by remember { mutableStateOf("") }
    var currentDateStr by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val wib = TimeZone.of("Asia/Jakarta")
        val daysId = mapOf(
            "Monday" to "Senin", "Tuesday" to "Selasa", "Wednesday" to "Rabu",
            "Thursday" to "Kamis", "Friday" to "Jumat", "Saturday" to "Sabtu", "Sunday" to "Minggu"
        )
        val monthsId = mapOf(
            1 to "Januari", 2 to "Februari", 3 to "Maret", 4 to "April", 5 to "Mei", 6 to "Juni",
            7 to "Juli", 8 to "Agustus", 9 to "September", 10 to "Oktober", 11 to "November", 12 to "Desember"
        )
        fun pad(v: Int) = v.toString().padStart(2, '0')
        while (true) {
            val now: LocalDateTime = Clock.System.now().toLocalDateTime(wib)
            currentTimeStr = "${pad(now.hour)}:${pad(now.minute)}:${pad(now.second)} WIB"
            currentDateStr = "${daysId[now.dayOfWeek.name] ?: now.dayOfWeek.name}, " +
                "${now.dayOfMonth} ${monthsId[now.monthNumber] ?: now.month.name} ${now.year}"
            delay(1000)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        containerColor = Slate950
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Header Section
                item {
                    HeaderSection(
                        dateStr = currentDateStr,
                        timeStr = currentTimeStr,
                        logoEmoji = state.logoEmoji,
                        onLogoTap = { viewModel.openLogoDialog() },
                        onOpenExtraction = { viewModel.openExtractDialog() },
                        onAddTask = { viewModel.openAddTask() },
                        onExportCsv = {
                            val csvData = viewModel.exportToCsvContent()
                            shareText(csvData, "Ekspor Joblist Dapur MBG ke CSV")
                        }
                    )
                }

                // 2. Operational Progress Summary
                item {
                    ProgressSection(state = state)
                }

                // 3. HACCP Talenan Cutting Board SOP Rules
                item {
                    CuttingBoardRuleSection()
                }

                // 4. Menu Harian & Target Info Card
                item {
                    MenuSection(
                        state = state,
                        onDayChange = { viewModel.setSelectedMenuDay(it) },
                        onEditMenu = { viewModel.openMenuDialog() }
                    )
                }

                // 5. HACCP Food Safety Checklist
                item {
                    HaccpChecklistSection(
                        state = state,
                        onToggle = { key, check -> viewModel.toggleHaccp(key, check) }
                    )
                }

                // 6. Filter Toolbar (Search, Category, Day, Shift, Status)
                item {
                    FilterToolbarSection(
                        state = state,
                        onSearchChange = { viewModel.setSearchQuery(it) },
                        onCategorySelect = { viewModel.setCategoryFilter(it) },
                        onDaySelect = { viewModel.setDayFilter(it) },
                        onShiftSelect = { viewModel.setShiftFilter(it) },
                        onStatusSelect = { viewModel.setStatusFilter(it) },
                        onMarkAllDone = { viewModel.markAllVisibleAsDone() },
                        onResetDefault = { viewModel.resetToDefaultData() }
                    )
                }

                // 7. Task List Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📋 Daftar Joblist Tim Persiapan",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Slate800)
                                .border(1.dp, Slate700, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${state.filteredTasks.size} Tugas",
                                color = Emerald300,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                // 8. Task Cards
                if (state.filteredTasks.isEmpty()) {
                    item {
                        EmptyStateSection()
                    }
                } else {
                    items(
                        items = state.filteredTasks,
                        key = { it.id }
                    ) { task ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            TaskCard(
                                task = task,
                                onToggleDone = { viewModel.toggleTaskDone(task) },
                                onStatusChange = { newStatus -> viewModel.updateTaskStatus(task.id, newStatus) },
                                onEdit = { viewModel.openEditTask(task) },
                                onDelete = { viewModel.deleteTask(task.id) }
                            )
                        }
                    }
                }
            }

            // Toast Floating Banner
            AnimatedVisibility(
                visible = state.toastMessage != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
            ) {
                state.toastMessage?.let { msg ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Slate800)
                            .border(1.dp, Emerald800, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Emerald400,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = msg,
                            color = Slate100,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(
                            onClick = { viewModel.clearToast() },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Tutup", tint = Slate400)
                        }
                    }
                }
            }
        }
    }

    // Modal Dialogs
    if (state.showExtractDialog) {
        AutoExtractionDialog(
            previewResult = state.extractionPreview,
            isExtracting = state.isExtracting,
            onDismiss = { viewModel.closeExtractDialog() },
            onExtractText = { viewModel.previewExtraction(it) },
            onConfirmSave = { replace -> viewModel.confirmSaveExtractedData(replace) }
        )
    }

    if (state.showTaskDialog) {
        TaskEditDialog(
            initialTask = state.editingTask,
            onDismiss = { viewModel.closeTaskDialog() },
            onSave = { title, day, cat, shift, assignee, targetTime, priority, status ->
                viewModel.saveTask(title, day, cat, shift, assignee, targetTime, priority, status)
            }
        )
    }

    if (state.showMenuDialog) {
        val currentMenu = state.currentMenu
        MenuEditDialog(
            day = state.selectedMenuDay,
            initialPorsi = currentMenu?.porsi ?: "2.503 Porsi",
            initialJamKirim = currentMenu?.jamKirim ?: "07:45 WIB | 08:40 WIB | 10:00 WIB",
            initialMenuList = currentMenu?.menuList ?: "",
            onDismiss = { viewModel.closeMenuDialog() },
            onSave = { day, porsi, jam, list ->
                viewModel.saveMenu(day, porsi, jam, list)
            }
        )
    }
}

@Composable
private fun HeaderSection(
    dateStr: String,
    timeStr: String,
    logoEmoji: String,
    onLogoTap: () -> Unit,
    onOpenExtraction: () -> Unit,
    onAddTask: () -> Unit,
    onExportCsv: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo Icon (ketuk untuk kustomisasi garpu & sendok)
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(listOf(Emerald600, Teal500))
                        )
                        .clickable(onClick = onLogoTap)
                        .testTag("app_logo_box"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = logoEmoji,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Default
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SPPG SUNGAI PASIR 1 MERAL",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        IconButton(
                            onClick = onLogoTap,
                            modifier = Modifier.size(26.dp).testTag("btn_edit_logo")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Kustomisasi Logo",
                                tint = Slate400,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Emerald950)
                            .border(1.dp, Emerald800, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "YAYASAN BIDADARI INDONESIA GEMILANG",
                            color = Emerald300,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Text(
                        text = "Sistem Operasional & Joblist Persiapan MBG",
                        color = Slate400,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Clock & Date Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Slate950)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateStr.ifEmpty { "Memuat tanggal..." },
                    color = Slate300,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = timeStr.ifEmpty { "00:00:00 WIB" },
                    color = Emerald400,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Actions Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Auto Extraction Button (Prominent)
                Button(
                    onClick = onOpenExtraction,
                    modifier = Modifier
                        .weight(1.3f)
                        .height(40.dp)
                        .testTag("btn_auto_extract"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald600)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ekstraksi Teks (AI)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Add Task Button
                Button(
                    onClick = onAddTask,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .testTag("btn_add_task_header"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Slate800)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Emerald400
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tambah", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate100)
                }

                // Export CSV Button
                IconButton(
                    onClick = onExportCsv,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Slate800)
                        .border(1.dp, Slate700, RoundedCornerShape(10.dp))
                        .testTag("btn_export_csv")
                ) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = "Ekspor CSV",
                        tint = Emerald400,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressSection(state: DashboardUiState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Status Progres Operasional",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        val statusText = if (state.isReadyForDistribution) "✅ Siap Distribusi" else "Sedang Berjalan"
                        val statusBg = if (state.isReadyForDistribution) Emerald950 else Amber950
                        val statusCol = if (state.isReadyForDistribution) Emerald300 else Amber400

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(statusBg)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = statusText,
                                color = statusCol,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "Persentase penyelesaian joblist persiapan hari ini secara realtime",
                        color = Slate400,
                        fontSize = 10.sp
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${state.progressPercent}%",
                        color = Emerald400,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "${state.completedCount} dari ${state.totalCount} selesai",
                        color = Emerald300,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { (state.progressPercent.toFloat() / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Emerald500,
                trackColor = Slate950
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Counter cards: Total, Belum, Proses, Selesai
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    title = "Total",
                    count = state.totalCount.toString(),
                    textColor = Color.White,
                    bgColor = Slate950,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Belum",
                    count = state.pendingCount.toString(),
                    textColor = Slate300,
                    bgColor = Slate950,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Proses",
                    count = state.inProgressCount.toString(),
                    textColor = Amber400,
                    bgColor = Amber950.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Selesai",
                    count = state.completedCount.toString(),
                    textColor = Emerald400,
                    bgColor = Emerald950.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    count: String,
    textColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(1.dp, Slate800, RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title.uppercase(), color = Slate400, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(text = count, color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun CuttingBoardRuleSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Teal400,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Standar HACCP: Aturan Warna Talenan",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Cegah Kontaminasi",
                    color = Slate400,
                    fontSize = 9.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CuttingBoardChip("MERAH", "Sapi/Kambing Mentah", Color(0xFFDC2626))
                CuttingBoardChip("KUNING", "Unggas/Ayam Mentah", Color(0xFFF59E0B))
                CuttingBoardChip("HIJAU", "Sayur & Buah", Color(0xFF10B981))
                CuttingBoardChip("BIRU", "Ikan & Seafood", Color(0xFF0EA5E9))
                CuttingBoardChip("COKELAT", "Daging Matang", Color(0xFFD97706))
                CuttingBoardChip("PUTIH", "Susu & Roti", Color(0xFFE2E8F0))
            }
        }
    }
}

@Composable
private fun CuttingBoardChip(name: String, desc: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Slate950)
            .border(1.dp, Slate800, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(text = name, color = color, fontSize = 9.sp, fontWeight = FontWeight.Black)
                Text(text = desc, color = Slate400, fontSize = 8.sp)
            }
        }
    }
}

@Composable
private fun MenuSection(
    state: DashboardUiState,
    onDayChange: (String) -> Unit,
    onEditMenu: () -> Unit
) {
    val currentMenu = state.currentMenu
    val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = androidx.compose.foundation.BorderStroke(1.dp, Amber950)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Selector & Edit Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Amber950)
                            .border(1.dp, Amber500, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "🍽️ Target & Menu Harian",
                            color = Amber400,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                IconButton(
                    onClick = onEditMenu,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Menu",
                        tint = Amber400,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Day Selector Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                days.forEach { day ->
                    val isSelected = state.selectedMenuDay.equals(day, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Amber500 else Slate950)
                            .border(1.dp, if (isSelected) Amber500 else Slate800, RoundedCornerShape(8.dp))
                            .clickable { onDayChange(day) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "📅 $day",
                            color = if (isSelected) Slate950 else Slate300,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Info: Target Porsi & Jam Kirim
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Slate950)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("TARGET PORSI", color = Slate400, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = currentMenu?.porsi ?: "2.503 Porsi",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Slate950)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("JADWAL DISTRIBUSI", color = Slate400, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = currentMenu?.jamKirim ?: "07:45 | 08:40 | 10:00 WIB",
                            color = Amber400,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Detailed Menu List
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Slate950)
                    .border(1.dp, Slate800, RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = currentMenu?.menuList?.ifEmpty { "Belum ada rincian menu." } ?: "Belum ada menu.",
                    color = Slate200,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun HaccpChecklistSection(
    state: DashboardUiState,
    onToggle: (String, Boolean) -> Unit
) {
    val haccp = state.haccpCheck

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = androidx.compose.foundation.BorderStroke(1.dp, Purple950)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Purple950)
                            .border(1.dp, Purple800, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "🔬 Cek Poin Keamanan Pangan & Sanitasi (HACCP)",
                            color = Purple300,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text("SOP Wajib SPPG", color = Slate400, fontSize = 9.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                HaccpCheckboxItem(
                    label = "Suhu Cold Storage / Freezer (-18°C)",
                    checked = haccp.coldStorageOk,
                    onCheckedChange = { onToggle("coldStorage", it) }
                )
                HaccpCheckboxItem(
                    label = "Sample Testing Organoleptik OK",
                    checked = haccp.organolepticOk,
                    onCheckedChange = { onToggle("organoleptic", it) }
                )
                HaccpCheckboxItem(
                    label = "Sanitasi Alat & Sterilisasi OK",
                    checked = haccp.sanitationOk,
                    onCheckedChange = { onToggle("sanitation", it) }
                )
                HaccpCheckboxItem(
                    label = "Pemasangan Label Jam Batas Makan",
                    checked = haccp.labelOk,
                    onCheckedChange = { onToggle("label", it) }
                )
            }
        }
    }
}

@Composable
private fun HaccpCheckboxItem(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Slate950)
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Emerald500,
                uncheckedColor = Slate600
            )
        )
        Text(
            text = label,
            color = if (checked) Emerald300 else Slate300,
            fontSize = 11.sp,
            fontWeight = if (checked) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun FilterToolbarSection(
    state: DashboardUiState,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onDaySelect: (String) -> Unit,
    onShiftSelect: (String) -> Unit,
    onStatusSelect: (String) -> Unit,
    onMarkAllDone: () -> Unit,
    onResetDefault: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Row 1: Search Field
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Cari tugas, bahan baku, atau PJ...", fontSize = 12.sp, color = Slate500) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Slate400, modifier = Modifier.size(18.dp))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_tasks_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Slate200,
                    focusedContainerColor = Slate950,
                    unfocusedContainerColor = Slate950,
                    focusedBorderColor = Emerald500,
                    unfocusedBorderColor = Slate800
                ),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
            )

            // Row 2: Category Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Kategori:",
                    color = Slate400,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                FilterChipItem("Semua", "all", state.selectedCategory == "all") { onCategorySelect("all") }
                FilterChipItem("🥦 Persiapan Bahan", "Persiapan Bahan", state.selectedCategory == "Persiapan Bahan") { onCategorySelect("Persiapan Bahan") }
                FilterChipItem("🧄 Bumbu & Racikan", "Bumbu & Racikan", state.selectedCategory == "Bumbu & Racikan") { onCategorySelect("Bumbu & Racikan") }
                FilterChipItem("✨ Kebersihan", "Kebersihan & Sanitasi", state.selectedCategory == "Kebersihan & Sanitasi") { onCategorySelect("Kebersihan & Sanitasi") }
                FilterChipItem("🛡️ Quality & Safety", "Quality & Safety Check", state.selectedCategory == "Quality & Safety Check") { onCategorySelect("Quality & Safety Check") }
                FilterChipItem("🍱 Packing", "Packing & Distribusi Internal", state.selectedCategory == "Packing & Distribusi Internal") { onCategorySelect("Packing & Distribusi Internal") }
            }

            // Row 3: Day Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Hari:",
                    color = Slate400,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                FilterChipItem("Semua Hari", "all", state.selectedDay == "all") { onDaySelect("all") }
                FilterChipItem("📅 Senin", "Senin", state.selectedDay == "Senin") { onDaySelect("Senin") }
                FilterChipItem("📅 Selasa", "Selasa", state.selectedDay == "Selasa") { onDaySelect("Selasa") }
                FilterChipItem("📅 Rabu", "Rabu", state.selectedDay == "Rabu") { onDaySelect("Rabu") }
                FilterChipItem("📅 Kamis", "Kamis", state.selectedDay == "Kamis") { onDaySelect("Kamis") }
                FilterChipItem("📅 Jumat", "Jumat", state.selectedDay == "Jumat") { onDaySelect("Jumat") }
            }

            // Row 4: Shift Filter Chips & Status Filter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Shift:",
                    color = Slate400,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                FilterChipItem("Semua Shift", "all", state.selectedShift == "all") { onShiftSelect("all") }
                FilterChipItem("🌙 Shift 1 (Subuh)", "Shift 1 (Malam/Subuh)", state.selectedShift == "Shift 1 (Malam/Subuh)") { onShiftSelect("Shift 1 (Malam/Subuh)") }
                FilterChipItem("☀️ Shift 2 (Pagi)", "Shift 2 (Pagi/Siang)", state.selectedShift == "Shift 2 (Pagi/Siang)") { onShiftSelect("Shift 2 (Pagi/Siang)") }
                FilterChipItem("🌆 Shift 3 (Sore)", "Shift 3 (Sore/Malam)", state.selectedShift == "Shift 3 (Sore/Malam)") { onShiftSelect("Shift 3 (Sore/Malam)") }
            }

            // Row 5: Action buttons: Selesaikan Tampil & Reset Default
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onMarkAllDone,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Emerald950,
                        contentColor = Emerald300
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("btn_mark_all_done")
                ) {
                    Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Selesaikan Tampil", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onResetDefault,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Slate300
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate700),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("btn_reset_default")
                ) {
                    Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reset Default", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    value: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Emerald600 else Slate950)
            .border(1.dp, if (isSelected) Emerald500 else Slate800, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else Slate300,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun EmptyStateSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Slate800),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    tint = Slate500,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Tidak Ada Tugas Ditemukan",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Coba sesuaikan kata kunci pencarian atau ganti filter hari, kategori, dan shift di atas.",
                color = Slate400,
                fontSize = 11.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
