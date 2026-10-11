package com.example.data.repository

import com.example.data.extraction.AutoTextExtractor
import com.example.data.extraction.ExtractionResult
import com.example.data.local.KitchenDao
import com.example.data.model.DailyMenuEntity
import com.example.data.model.ExtractionRecordEntity
import com.example.data.model.HaccpCheckEntity
import com.example.data.model.KitchenTaskEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class KitchenRepository(
    private val dao: KitchenDao,
    private val ioDispatcher: CoroutineDispatcher
) {

    val allTasks: Flow<List<KitchenTaskEntity>> = dao.getAllTasks()
    val allMenus: Flow<List<DailyMenuEntity>> = dao.getAllMenus()
    val haccpCheck: Flow<HaccpCheckEntity?> = dao.getHaccpCheck()
    val extractionHistory: Flow<List<ExtractionRecordEntity>> = dao.getAllExtractions()

    suspend fun initializeDatabaseIfEmpty() = withContext(ioDispatcher) {
        val taskCount = dao.getTaskCount()
        if (taskCount == 0) {
            val defaultTasks = getDefaultInitialTasks()
            dao.insertTasks(defaultTasks)
        }

        val menuCount = dao.getMenuCount()
        if (menuCount == 0) {
            val defaultMenus = getDefaultInitialMenus()
            dao.insertMenus(defaultMenus)
        }
    }

    suspend fun extractAndSave(rawText: String): ExtractionResult = withContext(ioDispatcher) {
        val result = AutoTextExtractor.extract(rawText)

        if (result.tasks.isNotEmpty()) {
            dao.insertTasks(result.tasks)
        }
        if (result.menus.isNotEmpty()) {
            dao.insertMenus(result.menus)
        }

        // Record history
        dao.insertExtraction(
            ExtractionRecordEntity(
                sourceSnippet = result.rawTextSnippet,
                tasksExtracted = result.tasks.size,
                menusExtracted = result.menus.size,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
        )

        result
    }

    suspend fun insertTask(task: KitchenTaskEntity) = withContext(ioDispatcher) {
        dao.insertTask(task)
    }

    suspend fun updateTask(task: KitchenTaskEntity) = withContext(ioDispatcher) {
        dao.updateTask(task)
    }

    suspend fun updateTaskStatus(id: String, status: String) = withContext(ioDispatcher) {
        dao.updateTaskStatus(id, status)
    }

    suspend fun deleteTask(id: String) = withContext(ioDispatcher) {
        dao.deleteTaskById(id)
    }

    suspend fun markTasksAsDone(ids: List<String>) = withContext(ioDispatcher) {
        for (id in ids) {
            dao.updateTaskStatus(id, "Selesai")
        }
    }

    suspend fun resetToDefaultData() = withContext(ioDispatcher) {
        dao.deleteAllTasks()
        dao.insertTasks(getDefaultInitialTasks())
        dao.insertMenus(getDefaultInitialMenus())
        dao.upsertHaccpCheck(HaccpCheckEntity(id = 1, coldStorageOk = false, organolepticOk = false, sanitationOk = false, labelOk = false))
    }

    suspend fun upsertMenu(menu: DailyMenuEntity) = withContext(ioDispatcher) {
        dao.upsertMenu(menu)
    }

    suspend fun upsertHaccpCheck(haccp: HaccpCheckEntity) = withContext(ioDispatcher) {
        dao.upsertHaccpCheck(haccp)
    }

    companion object {
        fun getDefaultInitialTasks(): List<KitchenTaskEntity> {
            return listOf(
                KitchenTaskEntity(
                    id = "1",
                    title = "🔸️Quality Receiving | Penerimaan & Sortir Bahan Baku Segar (🥬️Sayur,🥩️Daging Sapi,🍗️Ayam,🐠️Ikan & 🍓️Buah)",
                    day = "Semua Hari",
                    category = "Quality & Safety Check",
                    shift = "Shift 1 (Malam/Subuh)",
                    assignee = "Pak Rete & Fathur",
                    targetTime = "16:30 WIB",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 1
                ),
                KitchenTaskEntity(
                    id = "2",
                    title = "🔸️Pencucian, Sterilisasi & Potong Sayuran & Nabati",
                    day = "Semua Hari",
                    category = "Persiapan Bahan",
                    shift = "Shift 1 (Malam/Subuh)",
                    assignee = "Riska & Tim",
                    targetTime = "20:00 WIB - 04:00 WIB",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 2
                ),
                KitchenTaskEntity(
                    id = "3",
                    title = "🔸️🧄️🧅️Kupas Bawang, Sortir & Sterilisasi Buah 🍇️🍉️🍓️🍌️🍎️🍈️",
                    day = "Semua Hari",
                    category = "Persiapan Bahan",
                    shift = "Shift 1 (Malam/Subuh)",
                    assignee = "Mami & Nanda",
                    targetTime = "03:00 WIB - 11:00 WIB",
                    priority = "Sedang",
                    status = "Belum Dimulai",
                    sortOrder = 3
                ),
                KitchenTaskEntity(
                    id = "4",
                    title = "🔸️Meracik Bumbu, Tumisan, Deep Fry, Marinasi, Brining & Pengolahan Saos",
                    day = "Semua Hari",
                    category = "Bumbu & Racikan",
                    shift = "Shift 1 (Malam/Subuh)",
                    assignee = "Wawan & Tim",
                    targetTime = "01:00 WIB - Until Finish",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 4
                ),
                KitchenTaskEntity(
                    id = "5",
                    title = "🔸️Meracik Bumbu, Adonan, Ungkepan & Penggorengan | Goreng Tahu, (Running Persiapan & Koki)",
                    day = "Senin",
                    category = "Bumbu & Racikan",
                    shift = "Shift 1 (Malam/Subuh)",
                    assignee = "Meta & Ayu",
                    targetTime = "20:00 WIB - 04:00 WIB",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 5
                ),
                KitchenTaskEntity(
                    id = "6",
                    title = "🔸️Meracik Bumbu, Adonan, Ungkepan & Penggorengan | Goreng Kacang & Teri, (Running Persiapan & Koki)",
                    day = "Selasa",
                    category = "Bumbu & Racikan",
                    shift = "Shift 1 (Malam/Subuh)",
                    assignee = "Meta & Ayu",
                    targetTime = "22:00 WIB - 06:00 WIB",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 6
                ),
                KitchenTaskEntity(
                    id = "7",
                    title = "🔸️Rendam,Boiling/Rebus Telur Tanpa Garam & Kupas Telur Ayam 2.280 Butir",
                    day = "Senin",
                    category = "Persiapan Bahan",
                    shift = "Shift 1 (Malam/Subuh)",
                    assignee = "Alloy & Ruben",
                    targetTime = "20:00 WIB - 04:00 WIB",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 7
                ),
                KitchenTaskEntity(
                    id = "8",
                    title = "🔸️Ayam Fillet 200 kg Potong Finger Porsi Kecil 2, Porsi Besar 3",
                    day = "Selasa",
                    category = "Persiapan Bahan",
                    shift = "Shift 1 (Malam/Subuh)",
                    assignee = "Riska & Tim",
                    targetTime = "20:00 WIB - 04:00 WIB",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 8
                ),
                KitchenTaskEntity(
                    id = "9",
                    title = "🔸️🧅️Bawang Merah Kupas 12kg (8kg Blender,4kg iris) 🔸️🧄️Bawang Putih Kupas 10kg (6kg Blender,4kg iris) 🔸️🥬️Daun Bawang 6kg (3kg Iris Serong Tipis,3kg Cincang) 🔸️🧄️Bawang Bombai 5kg iris 🔸️🍃️Daun Jeruk 150gram Buang Batang 🔸️🌶️Cabe Merah Padang 500gram Blender, 1kg iris Serong 🔸️🌶️Cabe Merah Besar 500gram Blender 🔸️🥔️Kentang Bukit Sisa Digudang Kupas Kulit & Cuci Potong Dadu Kecil,",
                    day = "Senin",
                    category = "Persiapan Bahan",
                    shift = "Shift 1 (Malam/Subuh)",
                    assignee = "Riska & Tim",
                    targetTime = "20:00 WIB - 04:00 WIB",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 9
                ),
                KitchenTaskEntity(
                    id = "10",
                    title = "🔸️🧅️Bawang Merah Kupas 11kg Blender 🔸️(🧄️Bawang Putih Kupas 9kg Blender) 🔸️(🌶️Cabe Merah Padang 500gram Blender) 🔸️(🌶️Cabe Merah Kering 500gram Blender) 🔸️(🍅️Tomat Merah 6kg Potong Wedges(Untuk Sambal) 🔸️(🥒️Timun 40kg Kupas Kulit Jarak Potong Bulat) 🔸️(🥬️Selada 35kg Sortir & Cuci Bersih)",
                    day = "Selasa",
                    category = "Persiapan Bahan",
                    shift = "Shift 1 (Malam/Subuh)",
                    assignee = "Riska & Tim",
                    targetTime = "20:00 WIB - 04:00 WIB",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 10
                ),
                KitchenTaskEntity(
                    id = "11",
                    title = "Cuci Bersih Beras, Steam | Nasi Putih",
                    day = "Semua Hari",
                    category = "Persiapan Bahan",
                    shift = "Shift 1 (Malam/Subuh)",
                    assignee = "Ivan, Doni, Dendi, Ega (Split Shift)",
                    targetTime = "01:00 WIB - Until Finish",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 11
                ),
                KitchenTaskEntity(
                    id = "12",
                    title = "🔸️🍅️Tomat 30 kg Potong Bulat (Jam 03:30 Subuh)",
                    day = "Selasa",
                    category = "Persiapan Bahan",
                    shift = "Shift 1 (Malam/Subuh)",
                    assignee = "Tim Koki (Split)",
                    targetTime = "03:30 WIB - Until Finish",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 12
                ),
                KitchenTaskEntity(
                    id = "13",
                    title = "🔸️Sample Food Test (Uji Sensori,Rasa & Keamanan Organoleptik)",
                    day = "Semua Hari",
                    category = "Quality & Safety Check",
                    shift = "Shift 2 (Pagi/Siang)",
                    assignee = "Wawan & Tim",
                    targetTime = "04:35 WIB",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 13
                ),
                KitchenTaskEntity(
                    id = "14",
                    title = "🔸️Cek Suhu Cold Storage, Chiller | Gudang Basah & Kering",
                    day = "Semua Hari",
                    category = "Quality & Safety Check",
                    shift = "Shift 2 (Pagi/Siang)",
                    assignee = "Pak Rete",
                    targetTime = "16:30 WIB",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 14
                ),
                KitchenTaskEntity(
                    id = "15",
                    title = "🔸️Pelaporan Checklist Harian, Sisa Bahan Baku/Bumbu, Pelaporan Limbah & Kontrol Gas Elpiji",
                    day = "Semua Hari",
                    category = "Quality & Safety Check",
                    shift = "Shift 2 (Pagi/Siang)",
                    assignee = "Doni, Ivan, Dendi, Ega",
                    targetTime = "02:00 WIB - 11:00 WIB",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 15
                ),
                KitchenTaskEntity(
                    id = "16",
                    title = "🔸️Pemorsian & Pemasangan Label Jam Batas Aman Konsumsi Menu Sekolah & Posyandu (2.540 Porsi)",
                    day = "Semua Hari",
                    category = "Packing & Distribusi Internal",
                    shift = "Shift 2 (Pagi/Siang)",
                    assignee = "Tito & Tim Packing/Platting",
                    targetTime = "07:15 WIB",
                    priority = "Tinggi",
                    status = "Belum Dimulai",
                    sortOrder = 16
                ),
                KitchenTaskEntity(
                    id = "17",
                    title = "🔸️Cleaning & Sanitasi Keseluruhan Area Kerja Dapur",
                    day = "Semua Hari",
                    category = "Kebersihan & Sanitasi",
                    shift = "Shift 2 (Pagi/Siang)",
                    assignee = "Tim Kitchen",
                    targetTime = "11:00 WIB",
                    priority = "Sedang",
                    status = "Belum Dimulai",
                    sortOrder = 17
                ),
                KitchenTaskEntity(
                    id = "18",
                    title = "🔸️[SETIAP HARI JUMAT] Deep Clean & Sterilisasi Cold Storage (Freezer & Chiller)",
                    day = "Semua Hari",
                    category = "Kebersihan & Sanitasi",
                    shift = "Shift 2 (Pagi/Siang)",
                    assignee = "Tim Kitchen",
                    targetTime = "11:00 WIB",
                    priority = "Sedang",
                    status = "Belum Dimulai",
                    sortOrder = 18
                )
            )
        }

        fun getDefaultInitialMenus(): List<DailyMenuEntity> {
            return listOf(
                DailyMenuEntity(
                    day = "Senin",
                    porsi = "2.503 Porsi",
                    jamKirim = "07:45 WIB | 08:40 WIB | 10:00 WIB",
                    menuList = "SENIN - MENU SEKOLAH & POSYANDU :\n🥔 Potato Wedges\n🍗 Pangsit Ayam\n🫘 Edamame\n🥦 Mix Vegetable (Wortel, Jagung, Kacang Polong)\n🍇 Buah Anggur"
                ),
                DailyMenuEntity(
                    day = "Selasa",
                    porsi = "2.503 Porsi",
                    jamKirim = "07:45 WIB | 08:40 WIB | 10:00 WIB",
                    menuList = "SELASA - MENU SEKOLAH & POSYANDU :\n🍚 Nasi Putih\n🍗 Ayam Goreng Kecap\n🫘 Tahu Bacem\n🥦 Tumis Wortel & Jagung\n🌰️ Buah Salak"
                ),
                DailyMenuEntity(
                    day = "Rabu",
                    porsi = "2.503 Porsi",
                    jamKirim = "07:45 WIB | 08:40 WIB | 10:00 WIB",
                    menuList = "RABU - MENU SEKOLAH & POSYANDU :\n🍚 Nasi Putih\n🍗️ Ayam Tongseng\n🫘 Tempe Orek\n🥦 Tumis Labusiam & Jagung\n🍌️ Buah Pisang"
                ),
                DailyMenuEntity(
                    day = "Kamis",
                    porsi = "2.503 Porsi",
                    jamKirim = "07:45 WIB | 08:40 WIB | 10:00 WIB",
                    menuList = "KAMIS - MENU SEKOLAH & POSYANDU :\n🍚 Nasi Liwet\n🍗️ Ayam Goreng Bumbu Kuning\n🫘 Tahu Ungkep Goreng\n🥦 Selada & Timun\n🍇 Buah Anggur"
                ),
                DailyMenuEntity(
                    day = "Jumat",
                    porsi = "2.503 Porsi",
                    jamKirim = "07:45 WIB | 08:40 WIB | 10:00 WIB",
                    menuList = "JUMAT - MENU SEKOLAH & POSYANDU :\n🍚 Nasi Putih\n🍗 Telur Pindang\n🫘 Edamame\n🥦 Tumis Buncis & Wortel\n🧆 Buah Kelengkeng"
                )
            )
        }
    }
}
