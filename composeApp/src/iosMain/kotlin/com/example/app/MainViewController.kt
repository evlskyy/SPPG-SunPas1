package com.example.app

import platform.UIKit.UIViewController

import androidx.compose.ui.window.ComposeUIViewController
import com.example.platform.createDriverFactory
import com.example.ui.viewmodel.DashboardViewModel

/**
 * Entry point iOS — dipanggil dari Swift:
 *   ComAppViewController() / MainViewControllerKt.MainViewController()
 */
fun MainViewController(): platform.UIKit.UIViewController = run {
    val viewModel = DashboardViewModel.factory(createDriverFactory())
    ComposeUIViewController {
        App(viewModel = viewModel)
    }
}
