package com.lotus.demoflickrapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lotus.demoflickrapp.domain.model.Photo
import com.lotus.demoflickrapp.presentation.screen.detail.PhotoDetailScreen
import com.lotus.demoflickrapp.presentation.screen.home.HomeScreen
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun FlickrNavigation(
    navController: NavHostController = rememberNavController()
) {
    val gson = remember { Gson() }
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onPhotoClick = { photo ->
                    val photoJson = gson.toJson(photo)
                    val encodedJson = URLEncoder.encode(photoJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("detail/$encodedJson")
                }
            )
        }
        
        composable("detail/{photoJson}") { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("photoJson") ?: ""
            val photoJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
            val photo = gson.fromJson(photoJson, Photo::class.java)
            
            PhotoDetailScreen(
                photo = photo,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}