import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)

            implementation(libs.sqldelight.primitive.adapters)
            implementation(libs.sqldelight.coroutines)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.lifecycle.runtime.compose.multiplatform)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
            implementation(libs.kotlinx.coroutines.android)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
        }
    }
}

android {
    namespace = "com.example.shared"
    compileSdk = 36
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.example.data.db")
            // Adaptor primitif (Int/Long/Boolean) harus dinonaktifkan agar kode
            // yang dihasilkan memakai tipe kotlin.* standar dan portable di KMP.
            deriveSchemaFromMigrations.set(false)
            treatNullAsUnknownForEquality.set(true)
        }
    }
}

// Workaround: SQLDelight 2.x + primitive-adapters menghasilkan import `Int`/`Long`
// ilegal pada sumber commonMain. Bersihkan baris import tersebut dari file yang
// sudah digenerate (doFirst = sebelum compile, agar selalu diterapkan ulang).
plugins.withId("app.cash.sqldelight") {
    tasks.matching { it.name.contains("GenerateCommonMainAppDatabaseInterface", ignoreCase = true) }
        .configureEach {
            doFirst {
                val dir = layout.buildDirectory.dir("generated/sqldelight/code/AppDatabase/commonMain")
                    .get().asFile
                if (dir.exists()) {
                    dir.walkTopDown().filter { it.isFile && it.name.endsWith(".kt") }.forEach { f ->
                        val cleaned = f.readLines()
                            .filterNot { it.trim() in setOf(
                                "import Int", "import Long", "import Boolean", "import Float",
                                "import Double", "import Short", "import Byte", "import Char",
                                "import UByte", "import UShort", "import UInt", "import ULong",
                                "import Long as Long", "import Int as Int") }
                            .joinToString("\n")
                        f.writeText(cleaned + "\n")
                    }
                }
            }
        }
}

tasks.matching { it.name.contains("CompileKotlin", ignoreCase = true) }.configureEach {
    doFirst {
        val dir = layout.buildDirectory.dir("generated/sqldelight/code/AppDatabase/commonMain")
            .get().asFile
        if (dir.exists()) {
            dir.walkTopDown().filter { it.isFile && it.name.endsWith(".kt") }.forEach { f ->
                val lines = f.readLines()
                val bad = lines.filter { it.trim() in setOf(
                    "import Int", "import Long", "import Boolean", "import Float",
                    "import Double", "import Short", "import Byte", "import Char",
                    "import UByte", "import UShort", "import UInt", "import ULong",
                    "import Long as Long", "import Int as Int") }
                if (bad.isNotEmpty()) {
                    f.writeText(lines.filterNot { it in bad }.joinToString("\n") + "\n")
                }
            }
        }
    }
}
