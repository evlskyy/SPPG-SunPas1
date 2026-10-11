package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.platform.createDriverFactory
import com.example.platform.initAndroidPlatform
import com.example.ui.viewmodel.DashboardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initAndroidPlatform(this)
        val viewModel = DashboardViewModel.factory(createDriverFactory())
        setContent {
            App(viewModel = viewModel)
        }
    }
}
