package com.example.platform

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.data.db.AppDatabase
import com.example.data.db.DriverFactory
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import kotlin.native.concurrent.AtomicReference

actual object Platform {
    actual val name: String = "iOS"
}

actual fun randomUuid(): String = platform.Foundation.NSUUID().UUIDString()

private val topViewController = AtomicReference<platform.UIKit.UIViewController?>(null)

/** Opsional: set root VC dari SwiftUI wrapper bila share sheet butuh referensi eksplisit. */
fun setSharedRootViewController(vc: platform.UIKit.UIViewController?) {
    topViewController.value = vc
}

actual fun shareText(text: String, title: String) {
    var vc = topViewController.value
    if (vc == null) {
        val window = UIApplication.sharedApplication.keyWindow
        vc = window?.rootViewController
        while (vc?.presentedViewController != null) {
            vc = vc.presentedViewController
        }
    }
    val controller = vc ?: return
    val activityItems = listOf<Any?>(text)
    val avc = UIActivityViewController(activityItems = activityItems, applicationActivities = null)
    controller.presentViewController(avc, animated = true, completion = null)
}

actual fun createDriverFactory(): DriverFactory = object : DriverFactory {
    override fun createDriver(): SqlDriver =
        NativeSqliteDriver(AppDatabase.Schema, "dapur_mbg.db")
}

private var kvStore: com.example.data.db.KeyValueStore? = null

private fun getKeyValueStore(): com.example.data.db.KeyValueStore {
    return kvStore ?: com.example.data.db.KeyValueStore(
        NativeSqliteDriver(AppDatabase.Schema, "dapur_mbg.db")
    ).also { kvStore = it }
}

actual fun platformSaveString(key: String, value: String) {
    getKeyValueStore().putString(key, value)
}

actual fun platformGetString(key: String, default: String): String {
    return getKeyValueStore().getString(key, default)
}
