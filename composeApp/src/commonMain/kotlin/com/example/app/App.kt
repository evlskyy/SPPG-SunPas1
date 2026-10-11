package com.example.app

import androidx.compose.runtime.Composable
import com.example.ui.screens.DashboardScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.DashboardViewModel

/** Komposisi root aplikasi — dibagikan antara Android & iOS. */
@Composable
fun App(viewModel: DashboardViewModel) {
    MyApplicationTheme {
        DashboardScreen(viewModel = viewModel)
    }
}
