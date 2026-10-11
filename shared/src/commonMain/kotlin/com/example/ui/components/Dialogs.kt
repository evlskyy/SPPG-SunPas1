package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.extraction.AutoTextExtractor
import com.example.data.extraction.ExtractionResult
import com.example.data.model.KitchenTaskEntity
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.Amber950
import com.example.ui.theme.Emerald300
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald950
import com.example.ui.theme.Red400
import com.example.ui.theme.Red500
import com.example.ui.theme.Sky400
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950

@Composable
fun AutoExtractionDialog(
    previewResult: ExtractionResult?,
    isExtracting: Boolean,
    onDismiss: () -> Unit,
    onExtractText: (String) -> Unit,
    onConfirmSave: (replaceExisting: Boolean) -> Unit
) {
    var rawInputText by remember { mutableStateOf("") }
    var replaceExisting by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .heightIn(max = 680.dp)
                .testTag("auto_extraction_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = Slate900,
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Emerald950)
                                .border(1.dp, Emerald800, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Emerald400,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Ekstraksi Teks Otomatis",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Simpan Otomatis ke Database Lokal Room",
                                color = Emerald400,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = Slate400
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Aplikasi ini memindai teks dokumen persiapan dapur, mendeteksi jadwal, kategori, shift, penanggung jawab, jam target, menu harian, dan menyimpannya secara otomatis ke database lokal.",
                    color = Slate300,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Action buttons: Paste from clipboard / Preload original document text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val clipText = clipboardManager.getText()?.text.orEmpty()
                            rawInputText = if (clipText.isNotBlank()) {
                                if (rawInputText.isNotBlank()) "$rawInputText\n$clipText" else clipText
                            } else rawInputText
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("paste_clipboard_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Sky400,
                            containerColor = Slate950
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentPaste,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Tempel", fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    }

                    OutlinedButton(
                        onClick = {
                            rawInputText = AutoTextExtractor.ORIGINAL_DOCUMENT_TEXT
                        },
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("preload_document_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Emerald300,
                            containerColor = Slate950
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Emerald800)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Muat Teks Dokumen", fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            rawInputText = """
🔸️Pencucian & Sterilisasi Sayuran
Hari: Senin | Shift: Shift 1 (Malam/Subuh) | PJ: Riska & Tim | Target: 20:00 WIB - 04:00 WIB | Prioritas: Tinggi | Kategori: Persiapan Bahan

🔸️Goreng Ayam Crispy 250kg
Hari: Selasa | Shift: Shift 2 (Pagi/Siang) | PJ: Tim Koki | Target: 06:00 WIB | Prioritas: Tinggi | Kategori: Bumbu & Racikan

RABU - MENU SEKOLAH :
2.500 Porsi (08:00 WIB)
Menu: Nasi Kuning, Telur Balado, Timun, Buah Pisang
                            """.trimIndent()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("sample_notes_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Amber400,
                            containerColor = Slate950
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Amber500)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Sampel Catatan Dapur", fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Text Field Input (bisa diketik manual maupun ditempel dari clipboard)
                OutlinedTextField(
                    value = rawInputText,
                    onValueChange = { rawInputText = it },
                    label = { Text("Tempel Teks Dokumen / Joblist di Sini", fontSize = 12.sp) },
                    placeholder = {
                        Text(
                            "Contoh:\n1. Kupas Bawang 10kg, PJ: Mami, Shift: Subuh, Jam: 04:00 WIB\n2. Cek Suhu Freezer, Target: 05:00 WIB",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 160.dp, max = 260.dp)
                        .testTag("raw_text_input"),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 12,
                    minLines = 6,
                    scrollEnabled = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Slate200,
                        focusedContainerColor = Slate950,
                        unfocusedContainerColor = Slate950,
                        focusedBorderColor = Emerald500,
                        unfocusedBorderColor = Slate700,
                        focusedLabelColor = Emerald400,
                        unfocusedLabelColor = Slate400
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (rawInputText.isBlank()) "Belum ada teks" else "${rawInputText.length} karakter",
                        color = Slate500,
                        fontSize = 10.sp
                    )
                    if (rawInputText.isNotBlank()) {
                        Text(
                            text = "Hapus ✕",
                            color = Red400,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { rawInputText = "" }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Extract Button
                Button(
                    onClick = {
                        val toExtract = rawInputText.ifBlank { AutoTextExtractor.ORIGINAL_DOCUMENT_TEXT }
                        onExtractText(toExtract)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("start_extract_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Emerald600,
                        contentColor = Color.White
                    ),
                    enabled = !isExtracting
                ) {
                    if (isExtracting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Menganalisis & Mengekstrak...", fontSize = 12.sp)
                    } else {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ekstraksi Otomatis Sekarang", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Extraction Result Preview
                if (previewResult != null) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Slate950)
                            .border(1.dp, Emerald800, RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Emerald400,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Hasil Ekstraksi Dokumen",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Emerald950)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${previewResult.tasks.size} Tugas | ${previewResult.menus.size} Menu",
                                        color = Emerald300,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = previewResult.summary,
                                color = Emerald400,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Preview list of tasks (first 4 items)
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                previewResult.tasks.take(4).forEach { task ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Slate900)
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "•",
                                            color = Emerald400,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(end = 6.dp)
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = task.title,
                                                color = Slate100,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                maxLines = 1,
                                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "${task.day} | ${task.shift} | PJ: ${task.assignee} | ${task.targetTime}",
                                                color = Slate400,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                                if (previewResult.tasks.size > 4) {
                                    Text(
                                        text = "... dan ${previewResult.tasks.size - 4} tugas lainnya terdeteksi.",
                                        color = Slate400,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Option to replace or append
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { replaceExisting = !replaceExisting }
                            ) {
                                Checkbox(
                                    checked = replaceExisting,
                                    onCheckedChange = { replaceExisting = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Emerald500,
                                        uncheckedColor = Slate500
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Reset & Ganti data lama dengan hasil ekstraksi baru",
                                    color = Slate200,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Save to local database button
                            Button(
                                onClick = { onConfirmSave(replaceExisting) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("save_extracted_to_db_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Emerald500,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Save,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Simpan ke Database Lokal (Room)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskEditDialog(
    initialTask: KitchenTaskEntity?,
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        day: String,
        category: String,
        shift: String,
        assignee: String,
        targetTime: String,
        priority: String,
        status: String
    ) -> Unit
) {
    var title by remember { mutableStateOf(initialTask?.title ?: "") }
    var day by remember { mutableStateOf(initialTask?.day ?: "Semua Hari") }
    var category by remember { mutableStateOf(initialTask?.category ?: "Persiapan Bahan") }
    var shift by remember { mutableStateOf(initialTask?.shift ?: "Shift 1 (Malam/Subuh)") }
    var assignee by remember { mutableStateOf(initialTask?.assignee ?: "Riska & Tim") }
    var targetTime by remember { mutableStateOf(initialTask?.targetTime ?: "06:00 WIB") }
    var priority by remember { mutableStateOf(initialTask?.priority ?: "Sedang") }
    var status by remember { mutableStateOf(initialTask?.status ?: "Belum Dimulai") }

    val daysList = listOf("Semua Hari", "Senin", "Selasa", "Rabu", "Kamis", "Jumat")
    val categoriesList = listOf(
        "Persiapan Bahan",
        "Bumbu & Racikan",
        "Kebersihan & Sanitasi",
        "Quality & Safety Check",
        "Packing & Distribusi Internal"
    )
    val shiftsList = listOf(
        "Shift 1 (Malam/Subuh)",
        "Shift 2 (Pagi/Siang)",
        "Shift 3 (Sore/Malam)"
    )
    val prioritiesList = listOf("Tinggi", "Sedang", "Rendah")
    val statusesList = listOf("Belum Dimulai", "Dalam Proses", "Selesai")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .testTag("task_edit_dialog"),
            color = Slate900,
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (initialTask != null) "Edit Tugas Dapur" else "Tambah Tugas Baru",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup", tint = Slate400)
                    }
                }

                // Title field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nama Tugas & Instruksi", fontSize = 11.sp) },
                    placeholder = { Text("Contoh: Kupas kentang 20kg", fontSize = 11.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_task_title"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Slate200,
                        focusedContainerColor = Slate950,
                        unfocusedContainerColor = Slate950
                    )
                )

                // Day & Shift selectors
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SimpleDropdownField(
                        label = "Hari",
                        selected = day,
                        options = daysList,
                        onSelect = { day = it },
                        modifier = Modifier.weight(1f)
                    )
                    SimpleDropdownField(
                        label = "Shift",
                        selected = shift,
                        options = shiftsList,
                        onSelect = { shift = it },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Category selector
                SimpleDropdownField(
                    label = "Kategori Tugas",
                    selected = category,
                    options = categoriesList,
                    onSelect = { category = it },
                    modifier = Modifier.fillMaxWidth()
                )

                // Assignee & Target Time
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = assignee,
                        onValueChange = { assignee = it },
                        label = { Text("PJ", fontSize = 11.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_task_assignee"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Slate200,
                            focusedContainerColor = Slate950,
                            unfocusedContainerColor = Slate950
                        )
                    )
                    OutlinedTextField(
                        value = targetTime,
                        onValueChange = { targetTime = it },
                        label = { Text("Target Jam", fontSize = 11.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_task_time"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Slate200,
                            focusedContainerColor = Slate950,
                            unfocusedContainerColor = Slate950
                        )
                    )
                }

                // Priority & Status selectors
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SimpleDropdownField(
                        label = "Prioritas",
                        selected = priority,
                        options = prioritiesList,
                        onSelect = { priority = it },
                        modifier = Modifier.weight(1f)
                    )
                    SimpleDropdownField(
                        label = "Status",
                        selected = status,
                        options = statusesList,
                        onSelect = { status = it },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate300)
                    ) {
                        Text("Batal", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSave(title, day, category, shift, assignee, targetTime, priority, status)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                        modifier = Modifier.testTag("btn_save_task")
                    ) {
                        Text("Simpan Tugas", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun MenuEditDialog(
    day: String,
    initialPorsi: String,
    initialJamKirim: String,
    initialMenuList: String,
    onDismiss: () -> Unit,
    onSave: (day: String, porsi: String, jamKirim: String, menuList: String) -> Unit
) {
    var porsi by remember { mutableStateOf(initialPorsi) }
    var jamKirim by remember { mutableStateOf(initialJamKirim) }
    var menuList by remember { mutableStateOf(initialMenuList) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .testTag("menu_edit_dialog"),
            color = Slate900,
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Edit Menu & Target",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Hari: $day",
                            color = Amber400,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup", tint = Slate400)
                    }
                }

                OutlinedTextField(
                    value = porsi,
                    onValueChange = { porsi = it },
                    label = { Text("Total Target Porsi", fontSize = 11.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_menu_porsi"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Slate200,
                        focusedContainerColor = Slate950,
                        unfocusedContainerColor = Slate950
                    )
                )

                OutlinedTextField(
                    value = jamKirim,
                    onValueChange = { jamKirim = it },
                    label = { Text("Jadwal Distribusi / Jam Kirim", fontSize = 11.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_menu_jam"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Slate200,
                        focusedContainerColor = Slate950,
                        unfocusedContainerColor = Slate950
                    )
                )

                OutlinedTextField(
                    value = menuList,
                    onValueChange = { menuList = it },
                    label = { Text("Rincian Menu Makanan", fontSize = 11.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .testTag("input_menu_list"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Slate200,
                        focusedContainerColor = Slate950,
                        unfocusedContainerColor = Slate950
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate300)
                    ) {
                        Text("Batal", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSave(day, porsi, jamKirim, menuList) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Amber500),
                        modifier = Modifier.testTag("btn_save_menu")
                    ) {
                        Text("Simpan Menu", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate950)
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleDropdownField(
    label: String,
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Column {
            Text(text = label, color = Slate400, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(3.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Slate950)
                    .border(1.dp, Slate700, RoundedCornerShape(10.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selected,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Text(text = "▼", color = Slate400, fontSize = 9.sp)
                }
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Slate900)
        ) {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt, color = Slate200, fontSize = 12.sp) },
                    onClick = {
                        onSelect(opt)
                        expanded = false
                    }
                )
            }
        }
    }
}
