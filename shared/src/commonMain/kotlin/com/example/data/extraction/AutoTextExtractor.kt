package com.example.data.extraction

import com.example.data.model.DailyMenuEntity
import com.example.data.model.KitchenTaskEntity
import com.example.platform.randomUuid
import kotlinx.datetime.Clock

data class ExtractionResult(
    val tasks: List<KitchenTaskEntity>,
    val menus: List<DailyMenuEntity>,
    val summary: String,
    val rawTextSnippet: String
)

object AutoTextExtractor {

    val ORIGINAL_DOCUMENT_TEXT: String = """
Dashboard Joblist Tim Persiapan Dapur MBG - SPPG SUNGAI PASIR 1 MERAL
Yayasan Bidadari Indonesia Gemilang
Target Total Porsi: 2.541 Porsi
Jadwal Distribusi: 07:45 WIB | 08:40 WIB | 10:00 WIB

TARGET & MENU HARIAN:
- SENIN: 2.503 Porsi (07:45 | 08:40 | 10:00 WIB)
  Menu: Potato Wedges, Pangsit Ayam, Edamame, Mix Vegetable (Wortel,Jagung,Kacang Polong), Buah Anggur
- SELASA: 2.503 Porsi (07:45 | 08:40 | 10:00 WIB)
  Menu: Nasi Putih, Ayam Goreng Kecap, Tahu Bacem, Tumis Wortel & Jagung, Buah Salak
- RABU: 2.503 Porsi (07:45 | 08:40 | 10:00 WIB)
  Menu: Nasi Putih, Ayam Tongseng, Tempe Orek, Tumis Labusiam & Jagung, Buah Pisang
- KAMIS: 2.503 Porsi (07:45 | 08:40 | 10:00 WIB)
  Menu: Nasi Liwet, Ayam Goreng Bumbu Kuning, Tahu Ungkep Goreng, Selada & Timun, Buah Anggur
- JUMAT: 2.503 Porsi (07:45 | 08:40 | 10:00 WIB)
  Menu: Nasi Putih, Telur Pindang, Edamame, Tumis Buncis & Wortel, Buah Kelengkeng

STANDAR HACCP ATURAN TALENAN:
- Merah: Daging Sapi / Kambing Mentah
- Kuning: Daging Unggas / Ayam Mentah
- Hijau: Sayuran & Buah-buahan
- Biru: Ikan & Seafood Laut
- Cokelat: Daging Masak / Matang
- Putih: Produk Susu, Roti & Keju

DAFTAR TUGAS PERSIAPAN DAPUR:
1. Quality Receiving | Penerimaan & Sortir Bahan Baku Segar (Sayur, Daging Sapi, Ayam, Ikan & Buah)
   Hari: Semua Hari | Shift: Shift 1 (Malam/Subuh) | PJ: Pak Rete & Fathur | Target: 16:30 WIB | Prioritas: Tinggi | Kategori: Quality & Safety Check
2. Pencucian, Sterilisasi & Potong Sayuran & Nabati
   Hari: Semua Hari | Shift: Shift 1 (Malam/Subuh) | PJ: Riska & Tim | Target: 20:00 WIB - 04:00 WIB | Prioritas: Tinggi | Kategori: Persiapan Bahan
3. Kupas Bawang, Sortir & Sterilisasi Buah (Anggur, Semangka, Stroberi, Pisang, Apel, Melon)
   Hari: Semua Hari | Shift: Shift 1 (Malam/Subuh) | PJ: Mami & Nanda | Target: 03:00 WIB - 11:00 WIB | Prioritas: Sedang | Kategori: Persiapan Bahan
4. Meracik Bumbu, Tumisan, Deep Fry, Marinasi, Brining & Pengolahan Saos
   Hari: Semua Hari | Shift: Shift 1 (Malam/Subuh) | PJ: Wawan & Tim | Target: 01:00 WIB - Until Finish | Prioritas: Tinggi | Kategori: Bumbu & Racikan
5. Meracik Bumbu, Adonan, Ungkepan & Penggorengan | Goreng Tahu (Running Persiapan & Koki)
   Hari: Senin | Shift: Shift 1 (Malam/Subuh) | PJ: Meta & Ayu | Target: 20:00 WIB - 04:00 WIB | Prioritas: Tinggi | Kategori: Bumbu & Racikan
6. Meracik Bumbu, Adonan, Ungkepan & Penggorengan | Goreng Kacang & Teri (Running Persiapan & Koki)
   Hari: Selasa | Shift: Shift 1 (Malam/Subuh) | PJ: Meta & Ayu | Target: 22:00 WIB - 06:00 WIB | Prioritas: Tinggi | Kategori: Bumbu & Racikan
7. Rendam, Boiling/Rebus Telur Tanpa Garam & Kupas Telur Ayam 2.280 Butir
   Hari: Senin | Shift: Shift 1 (Malam/Subuh) | PJ: Alloy & Ruben | Target: 20:00 WIB - 04:00 WIB | Prioritas: Tinggi | Kategori: Persiapan Bahan
8. Ayam Fillet 200 kg Potong Finger Porsi Kecil 2, Porsi Besar 3
   Hari: Selasa | Shift: Shift 1 (Malam/Subuh) | PJ: Riska & Tim | Target: 20:00 WIB - 04:00 WIB | Prioritas: Tinggi | Kategori: Persiapan Bahan
9. Bawang Merah Kupas 12kg (8kg Blender, 4kg iris), Bawang Putih Kupas 10kg (6kg Blender, 4kg iris), Daun Bawang 6kg, Bawang Bombai 5kg, Kentang Bukit Kupas & Potong Dadu
   Hari: Senin | Shift: Shift 1 (Malam/Subuh) | PJ: Riska & Tim | Target: 20:00 WIB - 04:00 WIB | Prioritas: Tinggi | Kategori: Persiapan Bahan
10. Bawang Merah Kupas 11kg Blender, Bawang Putih Kupas 9kg, Cabe Padang, Cabe Kering, Tomat Merah 6kg Wedges, Timun 40kg, Selada 35kg Cuci Bersih
   Hari: Selasa | Shift: Shift 1 (Malam/Subuh) | PJ: Riska & Tim | Target: 20:00 WIB - 04:00 WIB | Prioritas: Tinggi | Kategori: Persiapan Bahan
11. Cuci Bersih Beras, Steam | Nasi Putih
   Hari: Semua Hari | Shift: Shift 1 (Malam/Subuh) | PJ: Ivan, Doni, Dendi, Ega (Split Shift) | Target: 01:00 WIB - Until Finish | Prioritas: Tinggi | Kategori: Persiapan Bahan
12. Tomat 30 kg Potong Bulat (Jam 03:30 Subuh)
   Hari: Selasa | Shift: Shift 1 (Malam/Subuh) | PJ: Tim Koki (Split) | Target: 03:30 WIB - Until Finish | Prioritas: Tinggi | Kategori: Persiapan Bahan
13. Sample Food Test (Uji Sensori, Rasa & Keamanan Organoleptik)
   Hari: Semua Hari | Shift: Shift 2 (Pagi/Siang) | PJ: Wawan & Tim | Target: 04:35 WIB | Prioritas: Tinggi | Kategori: Quality & Safety Check
14. Cek Suhu Cold Storage, Chiller | Gudang Basah & Kering
   Hari: Semua Hari | Shift: Shift 2 (Pagi/Siang) | PJ: Pak Rete | Target: 16:30 WIB | Prioritas: Tinggi | Kategori: Quality & Safety Check
15. Pelaporan Checklist Harian, Sisa Bahan Baku/Bumbu, Pelaporan Limbah & Kontrol Gas Elpiji
   Hari: Semua Hari | Shift: Shift 2 (Pagi/Siang) | PJ: Doni, Ivan, Dendi, Ega | Target: 02:00 WIB - 11:00 WIB | Prioritas: Tinggi | Kategori: Quality & Safety Check
16. Pemorsian & Pemasangan Label Jam Batas Aman Konsumsi Menu Sekolah & Posyandu (2.540 Porsi)
   Hari: Semua Hari | Shift: Shift 2 (Pagi/Siang) | PJ: Tito & Tim Packing/Platting | Target: 07:15 WIB | Prioritas: Tinggi | Kategori: Packing & Distribusi Internal
17. Cleaning & Sanitasi Keseluruhan Area Kerja Dapur
   Hari: Semua Hari | Shift: Shift 2 (Pagi/Siang) | PJ: Tim Kitchen | Target: 11:00 WIB | Prioritas: Sedang | Kategori: Kebersihan & Sanitasi
18. [SETIAP HARI JUMAT] Deep Clean & Sterilisasi Cold Storage (Freezer & Chiller)
   Hari: Semua Hari | Shift: Shift 2 (Pagi/Siang) | PJ: Tim Kitchen | Target: 11:00 WIB | Prioritas: Sedang | Kategori: Kebersihan & Sanitasi
""".trimIndent()

    fun extract(inputText: String): ExtractionResult {
        val cleanText = inputText.trim()
        val extractedTasks = mutableListOf<KitchenTaskEntity>()
        val extractedMenus = mutableListOf<DailyMenuEntity>()

        // 1. Check if the input contains JSON array format (e.g. copied from defaultTasks)
        if (cleanText.contains("defaultTasks") || (cleanText.startsWith("[") && cleanText.contains("title"))) {
            extractFromJsonLike(cleanText, extractedTasks)
        }

        // 2. Parse Structured or Semi-structured Tasks
        if (extractedTasks.isEmpty()) {
            extractFromTextBlocks(cleanText, extractedTasks)
        }

        // 3. Parse Daily Menus from text
        extractDailyMenus(cleanText, extractedMenus)

        // 4. Fallback: If nothing was parsed, create a task from the text itself
        if (extractedTasks.isEmpty() && cleanText.isNotBlank()) {
            extractedTasks.add(
                KitchenTaskEntity(
                    id = randomUuid(),
                    title = cleanText.take(150),
                    day = detectDay(cleanText),
                    category = detectCategory(cleanText),
                    shift = detectShift(cleanText),
                    assignee = detectAssignee(cleanText),
                    targetTime = detectTime(cleanText),
                    priority = detectPriority(cleanText),
                    status = "Belum Dimulai",
                    sortOrder = 0,
                    createdAt = Clock.System.now().toEpochMilliseconds()
                )
            )
        }

        val snippet = if (cleanText.length > 80) cleanText.take(80) + "..." else cleanText
        val summary = "Berhasil mengekstrak ${extractedTasks.size} tugas dan ${extractedMenus.size} menu harian dari dokumen."

        return ExtractionResult(
            tasks = extractedTasks,
            menus = extractedMenus,
            summary = summary,
            rawTextSnippet = snippet
        )
    }

    private fun extractFromJsonLike(text: String, outList: MutableList<KitchenTaskEntity>) {
        val objectRegex = Regex("""\{[^{}]*?"title":\s*['"](.*?)['"][^{}]*?\}""", RegexOption.DOT_MATCHES_ALL)
        val matches = objectRegex.findAll(text).toList()

        for ((index, match) in matches.withIndex()) {
            val block = match.value
            val title = extractJsonField(block, "title") ?: continue
            val id = extractJsonField(block, "id") ?: (index + 1).toString()
            val day = extractJsonField(block, "day") ?: "Semua Hari"
            val category = extractJsonField(block, "category") ?: "Persiapan Bahan"
            val shift = extractJsonField(block, "shift") ?: "Shift 1 (Malam/Subuh)"
            val assignee = extractJsonField(block, "assignee") ?: "Tim Dapur"
            val targetTime = extractJsonField(block, "targetTime") ?: "06:00 WIB"
            val priority = extractJsonField(block, "priority") ?: "Sedang"
            val status = extractJsonField(block, "status") ?: "Belum Dimulai"

            outList.add(
                KitchenTaskEntity(
                    id = id,
                    title = title,
                    day = day,
                    category = category,
                    shift = shift,
                    assignee = assignee,
                    targetTime = targetTime,
                    priority = priority,
                    status = status,
                    sortOrder = index,
                    createdAt = Clock.System.now().toEpochMilliseconds()
                )
            )
        }
    }

    private fun extractJsonField(block: String, key: String): String? {
        val pattern = Regex("""['"]?$key['"]?\s*:\s*['"](.*?)['"]""", RegexOption.IGNORE_CASE)
        val match = pattern.find(block)
        return match?.groupValues?.get(1)?.replace("\\n", "\n")?.trim()
    }

    private fun extractFromTextBlocks(text: String, outList: MutableList<KitchenTaskEntity>) {
        val lines = text.lines()
        var currentTitle: String? = null
        var currentDetails = StringBuilder()
        var taskIndex = 0

        fun flushCurrentTask() {
            val title = currentTitle?.trim()
            if (!title.isNullOrBlank()) {
                val combinedDetails = currentDetails.toString()
                val fullContext = "$title $combinedDetails"

                val task = KitchenTaskEntity(
                    id = (taskIndex + 1).toString(),
                    title = title,
                    day = detectDay(fullContext),
                    category = detectCategory(fullContext),
                    shift = detectShift(fullContext),
                    assignee = detectAssignee(fullContext),
                    targetTime = detectTime(fullContext),
                    priority = detectPriority(fullContext),
                    status = if (fullContext.contains("Selesai", ignoreCase = true)) "Selesai" else "Belum Dimulai",
                    sortOrder = taskIndex,
                    createdAt = Clock.System.now().toEpochMilliseconds()
                )
                outList.add(task)
                taskIndex++
            }
            currentTitle = null
            currentDetails.clear()
        }

        for (rawLine in lines) {
            val line = rawLine.trim()
            if (line.isBlank()) continue

            // Ignore top general headers
            if (line.startsWith("Dashboard", ignoreCase = true) ||
                line.startsWith("TARGET & MENU", ignoreCase = true) ||
                line.startsWith("STANDAR HACCP", ignoreCase = true) ||
                line.startsWith("DAFTAR TUGAS", ignoreCase = true) ||
                line.startsWith("Yayasan Bidadari", ignoreCase = true)) {
                continue
            }

            // Check if this line looks like a new task header (e.g. 1., 2., 🔸️, -, *, [ ])
            val isBulletTask = line.matches(Regex("""^(\d+[\.\)]\s*|🔸[️]?\s*|[-*•]\s*|\[[ xX]?\]\s*).*"""))
            val hasInlineDetails = line.contains("Hari:") || line.contains("Shift:") || line.contains("PJ:") || line.contains("Target:")

            if (isBulletTask && !hasInlineDetails) {
                flushCurrentTask()
                // Strip bullet marker
                val cleanedTitle = line.replaceFirst(Regex("""^(\d+[\.\)]\s*|🔸[️]?\s*|[-*•]\s*|\[[ xX]?\]\s*)"""), "").trim()
                currentTitle = cleanedTitle
            } else if (hasInlineDetails) {
                if (currentTitle == null) {
                    // Title might be in the same line before details
                    val parts = line.split(Regex("""(?=Hari:|Shift:|PJ:|Target:)"""))
                    if (parts.isNotEmpty()) {
                        val firstPart = parts[0].replaceFirst(Regex("""^(\d+[\.\)]\s*|🔸[️]?\s*|[-*•]\s*)"""), "").trim()
                        if (firstPart.isNotBlank()) {
                            currentTitle = firstPart
                        }
                    }
                }
                currentDetails.append(" ").append(line)
                flushCurrentTask()
            } else {
                if (currentTitle != null) {
                    currentDetails.append(" ").append(line)
                } else if (line.length > 5) {
                    // Potential standalone task line
                    currentTitle = line
                    flushCurrentTask()
                }
            }
        }
        flushCurrentTask()
    }

    private fun extractDailyMenus(text: String, outList: MutableList<DailyMenuEntity>) {
        val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat")
        for (day in days) {
            val dayRegex = Regex("""(?i)\b$day\b.*?[:\-]\s*([^\n]+(?:\n\s*Menu:\s*[^\n]+)?)""")
            val match = dayRegex.find(text)
            if (match != null) {
                val fullMatch = match.value
                val porsi = if (fullMatch.contains("Porsi", ignoreCase = true)) {
                    Regex("""\d+[\.,]?\d*\s*Porsi""", RegexOption.IGNORE_CASE).find(fullMatch)?.value ?: "2.503 Porsi"
                } else "2.503 Porsi"

                val jamKirim = if (fullMatch.contains("WIB", ignoreCase = true)) {
                    Regex("""\d{2}:\d{2}.*?WIB""").find(fullMatch)?.value ?: "07:45 WIB | 08:40 WIB | 10:00 WIB"
                } else "07:45 WIB | 08:40 WIB | 10:00 WIB"

                val menuListText = if (fullMatch.contains("Menu:", ignoreCase = true)) {
                    fullMatch.substringAfter("Menu:").trim()
                } else {
                    fullMatch.substringAfter(":").trim()
                }

                outList.add(
                    DailyMenuEntity(
                        day = day,
                        porsi = porsi,
                        jamKirim = jamKirim,
                        menuList = menuListText,
                        updatedAt = Clock.System.now().toEpochMilliseconds()
                    )
                )
            }
        }
    }

    private fun detectDay(text: String): String {
        val explicitMatch = Regex("""(?:Hari|Day)\s*:\s*([^|\n,;]+)""", RegexOption.IGNORE_CASE).find(text)
        if (explicitMatch != null) {
            val matched = explicitMatch.groupValues[1].trim()
            if (matched.isNotEmpty()) return matched
        }
        return when {
            text.contains("Semua Hari", ignoreCase = true) || text.contains("Rutin", ignoreCase = true) -> "Semua Hari"
            text.contains("Senin", ignoreCase = true) -> "Senin"
            text.contains("Selasa", ignoreCase = true) -> "Selasa"
            text.contains("Rabu", ignoreCase = true) -> "Rabu"
            text.contains("Kamis", ignoreCase = true) -> "Kamis"
            text.contains("Jumat", ignoreCase = true) -> "Jumat"
            else -> "Semua Hari"
        }
    }

    private fun detectCategory(text: String): String {
        val explicitMatch = Regex("""(?:Kategori|Category)\s*:\s*([^|\n,;]+)""", RegexOption.IGNORE_CASE).find(text)
        if (explicitMatch != null) {
            val matched = explicitMatch.groupValues[1].trim()
            if (matched.isNotEmpty()) return matched
        }
        val lower = text.lowercase()
        return when {
            lower.contains("quality") || lower.contains("safety") || lower.contains("receiving") ||
            lower.contains("sortir") || lower.contains("sample") || lower.contains("suhu") ||
            lower.contains("laporan") || lower.contains("checklist") -> "Quality & Safety Check"

            lower.contains("bumbu") || lower.contains("racik") || lower.contains("goreng") ||
            lower.contains("tumis") || lower.contains("adonan") || lower.contains("ungkep") ||
            lower.contains("saos") || lower.contains("marinasi") -> "Bumbu & Racikan"

            lower.contains("kebersihan") || lower.contains("sanitasi") || lower.contains("clean") ||
            lower.contains("sterilisasi") || lower.contains("sapu") || lower.contains("pel") -> "Kebersihan & Sanitasi"

            lower.contains("packing") || lower.contains("porsi") || lower.contains("label") ||
            lower.contains("distribusi") || lower.contains("platting") -> "Packing & Distribusi Internal"

            else -> "Persiapan Bahan"
        }
    }

    private fun detectShift(text: String): String {
        val explicitMatch = Regex("""(?:Shift)\s*:\s*([^|\n,;]+)""", RegexOption.IGNORE_CASE).find(text)
        if (explicitMatch != null) {
            val matched = explicitMatch.groupValues[1].trim()
            if (matched.isNotEmpty()) return matched
        }
        val lower = text.lowercase()
        return when {
            lower.contains("shift 2") || lower.contains("pagi/siang") || lower.contains("pagi") || lower.contains("siang") -> "Shift 2 (Pagi/Siang)"
            lower.contains("shift 3") || lower.contains("sore/malam") || lower.contains("sore") -> "Shift 3 (Sore/Malam)"
            else -> "Shift 1 (Malam/Subuh)"
        }
    }

    private fun detectAssignee(text: String): String {
        // Look for "PJ: [Name]" or "Assignee: [Name]"
        val pjMatch = Regex("""(?:PJ|Penanggung Jawab|Assignee)\s*:\s*([^|\n,;]+)""", RegexOption.IGNORE_CASE).find(text)
        if (pjMatch != null) {
            return pjMatch.groupValues[1].trim()
        }

        // Known personnel in Dapur MBG
        val lower = text.lowercase()
        return when {
            lower.contains("pak rete") || lower.contains("fathur") -> "Pak Rete & Fathur"
            lower.contains("riska") -> "Riska & Tim"
            lower.contains("mami") || lower.contains("nanda") -> "Mami & Nanda"
            lower.contains("wawan") -> "Wawan & Tim"
            lower.contains("meta") || lower.contains("ayu") -> "Meta & Ayu"
            lower.contains("alloy") || lower.contains("ruben") -> "Alloy & Ruben"
            lower.contains("ivan") || lower.contains("doni") || lower.contains("dendi") || lower.contains("ega") -> "Doni, Ivan, Dendi, Ega"
            lower.contains("tito") -> "Tito & Tim Packing/Platting"
            lower.contains("koki") -> "Tim Koki (Split)"
            lower.contains("kitchen") -> "Tim Kitchen"
            else -> "Tim Dapur MBG"
        }
    }

    private fun detectTime(text: String): String {
        val targetMatch = Regex("""Target\s*:\s*([^|\n]+)""", RegexOption.IGNORE_CASE).find(text)
        if (targetMatch != null) {
            return targetMatch.groupValues[1].trim()
        }

        val timeRangeMatch = Regex("""\d{1,2}[:.]\d{2}\s*(?:WIB)?\s*-\s*(?:\d{1,2}[:.]\d{2}\s*(?:WIB)?|Until\s*Finish)""", RegexOption.IGNORE_CASE).find(text)
        if (timeRangeMatch != null) {
            return timeRangeMatch.value.trim()
        }

        val singleTimeMatch = Regex("""\d{1,2}[:.]\d{2}\s*(?:WIB)?""", RegexOption.IGNORE_CASE).find(text)
        if (singleTimeMatch != null) {
            val t = singleTimeMatch.value.trim()
            return if (t.contains("WIB", ignoreCase = true)) t else "$t WIB"
        }

        return "06:00 WIB"
    }

    private fun detectPriority(text: String): String {
        val explicitMatch = Regex("""(?:Prioritas|Priority)\s*:\s*([^|\n,;]+)""", RegexOption.IGNORE_CASE).find(text)
        if (explicitMatch != null) {
            val matched = explicitMatch.groupValues[1].trim()
            if (matched.isNotEmpty()) return matched
        }
        val lower = text.lowercase()
        return when {
            lower.contains("prioritas: tinggi") || lower.contains("tinggi") || lower.contains("urgent") || lower.contains("kritis") -> "Tinggi"
            lower.contains("prioritas: rendah") || lower.contains("rendah") || lower.contains("santai") -> "Rendah"
            else -> "Sedang"
        }
    }
}
