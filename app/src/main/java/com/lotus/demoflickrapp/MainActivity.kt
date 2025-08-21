package com.lotus.demoflickrapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lotus.demoflickrapp.presentation.navigation.FlickrNavigation
import com.lotus.demoflickrapp.ui.theme.DemoFlickrAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoFlickrAppTheme {
                FlickrNavigation()
            }
        }
    }
}