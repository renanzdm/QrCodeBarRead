package com.renan.qrcodebarreader.presenter

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.renan.qrcodebarreader.presenter.home.HomePage
import com.renan.qrcodebarreader.presenter.home.pages.QrCodeGeneratePage

sealed class AppRoutes {
    companion object {
        const val homeRoute: String = "home"
        const val generateQrCodeRoute: String = "generateQrCodeRoute"
    }
}

@Composable
fun AppNavigationHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination =AppRoutes.homeRoute) {
        composable(AppRoutes.homeRoute) { HomePage(navController = navController) }
        composable(AppRoutes.generateQrCodeRoute) { QrCodeGeneratePage() }
    }
}


//fun NavGraphBuilder.homeGraph(navController: NavHostController) {
//    navigation(startDestination = AppRoutes.homeRoute, route = "home-graph") {
//        composable(AppRoutes.homeRoute) { HomePage() }
//    }
//}