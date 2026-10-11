# Dapur MBG — Dukungan Platform (Android & iOS)

Aplikasi **Dapur MBG** adalah aplikasi mobile untuk sistem operasional & joblist
persiapan dapur MBG SPPG Sungai Pasir 1 Meral, dengan ekstraksi teks otomatis
(Gemini/Firebase AI) dan database lokal.

## ✅ Android — Sudah Didukung (Build Aktif)

Proyek ini saat ini **sudah dapat dijalankan/di-build di Android**:

| Komponen | Status | Keterangan |
|---|---|---|
| Kerangka aplikasi | ✅ | Kotlin + Jetpack Compose (Material 3) |
| Build system | ✅ | Gradle (Kotlin DSL), `app/build.gradle.kts` |
| Minimum OS | ✅ | Android 7.0 (API 24) ke atas |
| Target SDK | ✅ | API 36 |
| Database lokal | ✅ | Room (`data/local/AppDatabase.kt`, `KitchenDao.kt`) |
| Ekstraksi teks AI | ✅ | Firebase AI (`firebase.ai`) + App Check |
| UI Dashboard | ✅ | `ui/screens/DashboardScreen.kt` + ViewModel |
| Pengujian | ✅ | Unit test (Robolectric) & instrumented test |

### Cara menjalankan di Android
```bash
# Hubungkan perangkat/halusinator emulator, lalu:
./gradlew assembleDebug        # build APK debug
./gradlew installDebug         # pasang ke perangkat
# atau buka proyek ini di Android Studio → Run 'app'
```
APK hasil build: `app/build/outputs/apk/debug/app-debug.apk`

## ⚠️ iOS — Belum Tersedia Pada Kode Saat Ini

Kode yang ada **hanya berjalan di Android** karena menggunakan API khusus Android:
- Jetpack Compose + `ComponentActivity` (MainActivity.kt)
- Room database (library AndroidX)
- Firebase AI / App Check SDK Android
- Resource XML Android (`res/values`, `AndroidManifest.xml`)

### Opsi agar aplikasi tampil juga di iOS

**Opsi A — Migrasi ke Kotlin Multiplatform (KMP) + Compose Multiplatform** *(direkomendasikan)*
Karena seluruh logika bisnis sudah ditulis dalam Kotlin, ini jalur paling efisien:
1. Buat modul `shared/` berisi model, repository, dan logika ekstraksi AI.
2. Ganti Room dengan **SQLDelight** (tersedia untuk iOS).
3. Pindahkan UI `DashboardScreen` ke modul `composeApp/` dengan
   **Compose Multiplatform** (`org.jetbrains.compose`).
4. Tambahkan target `iosArm64()` / `iosSimulatorArm64()` dan wrapper SwiftUI
   (`ComposeUIViewController`) pada folder `iosApp/`.
5. Untuk AI: gunakan REST endpoint Gemini via Ktor (berjalan di kedua platform).

**Opsi B — Rewrite native Swift/SwiftUI**
Membuat proyek iOS terpisah dengan SwiftUI + GRDB/CoreData. Biaya lebih besar
karena kode Kotlin tidak dapat dipakai ulang.

**Opsi C — Framework lintas-platform baru (Flutter / React Native)**
Menulis ulang total; hanya masuk akal jika ingin mengganti arah teknologi.

## Ringkasan

| Fitur | Android | iOS |
|---|---|---|
| Dashboard joblist dapur | ✅ | ❌ (butuh migrasi KMP) |
| Ekstraksi teks otomatis | ✅ | ❌ |
| Database lokal offline | ✅ | ❌ |
| Memerlukan perubahan kode? | Tidak | Ya (lihat Opsi A) |
