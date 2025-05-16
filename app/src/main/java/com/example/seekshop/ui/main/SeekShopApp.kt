package com.example.seekshop.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.seekshop.repository.LocationRepository
import com.example.seekshop.ui.location.LocationScreen
import com.example.seekshop.ui.search.SearchScreen
import javax.inject.Inject

@Composable
fun SeekShopApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "location"
    ) {
        composable("location") {
            LocationScreen(
                onLocationSelected = { locationId ->

                    navController.navigate("search")
                }
            )
        }
        composable("search") {
            SearchScreen()
        }
    }
}