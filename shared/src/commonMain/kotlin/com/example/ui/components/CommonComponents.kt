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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.KitchenTaskEntity
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.Amber950
import com.example.ui.theme.Emerald300
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald900
import com.example.ui.theme.Emerald950
import com.example.ui.theme.Indigo300
import com.example.ui.theme.Indigo800
import com.example.ui.theme.Indigo950
import com.example.ui.theme.Purple300
import com.example.ui.theme.Purple800
import com.example.ui.theme.Purple950
import com.example.ui.theme.Red400
import com.example.ui.theme.Red800
import com.example.ui.theme.Red950
import com.example.ui.theme.Sky300
import com.example.ui.theme.Sky800
import com.example.ui.theme.Sky950
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.Teal300
import com.example.ui.theme.Teal800
import com.example.ui.theme.Teal950

@Composable
fun DayBadge(day: String) {
    val isRoutine = day.equals("Semua Hari", ignoreCase = true)
    val bg = if (isRoutine) Indigo950 else Emerald950
    val border = if (isRoutine) Indigo800 else Emerald800
    val textCol = if (isRoutine) Indigo300 else Emerald300
    val label = if (isRoutine) "🔁 Rutin Harian" else "📅 Hari $day"

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            color = textCol,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CategoryBadge(category: String) {
    val (bg, border, textCol, emoji) = when (category) {
        "Persiapan Bahan" -> Quad(Emerald950, Emerald800, Emerald300, "🥦")
        "Bumbu & Racikan" -> Quad(Amber950, Amber500, Amber400, "🧄")
        "Kebersihan & Sanitasi" -> Quad(Sky950, Sky800, Sky300, "✨")
        "Quality & Safety Check" -> Quad(Purple950, Purple800, Purple300, "🛡️")
        "Packing & Distribusi Internal" -> Quad(Teal950, Teal800, Teal300, "🍱")
        else -> Quad(Slate800, Slate700, Slate300, "📋")
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = "$emoji $category",
            color = textCol,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun PriorityBadge(priority: String) {
    val (bg, border, textCol, emoji) = when (priority) {
        "Tinggi" -> Quad(Red950, Red800, Red400, "Tinggi 🔥")
        "Sedang" -> Quad(Amber950, Amber500, Amber400, "Sedang ⚡")
        else -> Quad(Slate800, Slate700, Slate400, "Rendah ☕")
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = emoji,
            color = textCol,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun TaskCard(
    task: KitchenTaskEntity,
    onToggleDone: () -> Unit,
    onStatusChange: (String) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDone = task.status == "Selesai"
    var statusMenuExpanded by remember { mutableStateOf(false) }

    val cardBg = if (isDone) Color(0xFF03261C) else Slate900
    val cardBorder = if (isDone) Emerald900 else Slate800

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("task_item_${task.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Row 1: Checkbox + Title + Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Circular Checkbox
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDone) Emerald600 else Slate950)
                        .border(1.5.dp, if (isDone) Emerald500 else Slate700, RoundedCornerShape(8.dp))
                        .clickable { onToggleDone() }
                        .testTag("check_task_${task.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selesai",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        color = if (isDone) Slate400 else Slate100,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Chips row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DayBadge(day = task.day)
                        CategoryBadge(category = task.category)
                        PriorityBadge(priority = task.priority)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("edit_task_${task.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Tugas",
                            tint = Slate400,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("delete_task_${task.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Hapus Tugas",
                            tint = Slate400,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row 2: PJ & Shift & Target Time Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Slate950)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Slate500,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = task.assignee,
                        color = Slate200,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(text = "•", color = Slate500, fontSize = 10.sp)
                    Text(
                        text = task.shift,
                        color = Emerald400,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Emerald400,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = task.targetTime,
                        color = Slate200,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row 3: Interactive Status Dropdown Selector
            Box {
                val statusBg = when (task.status) {
                    "Selesai" -> Emerald950
                    "Dalam Proses" -> Amber950
                    else -> Slate950
                }
                val statusTextCol = when (task.status) {
                    "Selesai" -> Emerald300
                    "Dalam Proses" -> Amber400
                    else -> Slate300
                }
                val statusBorder = when (task.status) {
                    "Selesai" -> Emerald800
                    "Dalam Proses" -> Amber500
                    else -> Slate700
                }
                val statusEmoji = when (task.status) {
                    "Selesai" -> "✅ Selesai"
                    "Dalam Proses" -> "🔄 Dalam Proses"
                    else -> "⏳ Belum Dimulai"
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(statusBg)
                        .border(1.dp, statusBorder, RoundedCornerShape(10.dp))
                        .clickable { statusMenuExpanded = true }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                        .testTag("status_select_${task.id}"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status: $statusEmoji",
                        color = statusTextCol,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Ganti ▼",
                        color = statusTextCol.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                DropdownMenu(
                    expanded = statusMenuExpanded,
                    onDismissRequest = { statusMenuExpanded = false },
                    modifier = Modifier.background(Slate900)
                ) {
                    DropdownMenuItem(
                        text = { Text("⏳ Belum Dimulai", color = Slate200, fontSize = 12.sp) },
                        onClick = {
                            onStatusChange("Belum Dimulai")
                            statusMenuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("🔄 Dalam Proses", color = Amber400, fontSize = 12.sp) },
                        onClick = {
                            onStatusChange("Dalam Proses")
                            statusMenuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("✅ Selesai", color = Emerald400, fontSize = 12.sp) },
                        onClick = {
                            onStatusChange("Selesai")
                            statusMenuExpanded = false
                        }
                    )
                }
            }
        }
    }
}
