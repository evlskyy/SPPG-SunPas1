package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.extraction.AutoTextExtractor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Dapur MBG", appName)
    }

    @Test
    fun `test auto text extraction parses tasks and menus`() {
        val sampleText = """
        🔸️Pencucian & Sterilisasi Sayuran
        Hari: Senin | Shift: Shift 1 (Malam/Subuh) | PJ: Riska & Tim | Target: 20:00 WIB - 04:00 WIB | Prioritas: Tinggi | Kategori: Persiapan Bahan

        SENIN - MENU SEKOLAH :
        2.503 Porsi (07:45 WIB)
        Menu: Potato Wedges, Pangsit Ayam, Edamame, Mix Vegetable, Buah Anggur
        """.trimIndent()

        val result = AutoTextExtractor.extract(sampleText)
        assertTrue(result.tasks.isNotEmpty())
        assertEquals("Pencucian & Sterilisasi Sayuran", result.tasks[0].title)
        assertEquals("Senin", result.tasks[0].day)
        assertEquals("Persiapan Bahan", result.tasks[0].category)
        assertEquals("Riska & Tim", result.tasks[0].assignee)

        assertTrue(result.menus.isNotEmpty())
        assertEquals("Senin", result.menus[0].day)
    }
}
