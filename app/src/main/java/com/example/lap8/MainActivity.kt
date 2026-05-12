package com.example.lap8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.lap8.navigation.MyNavigation
import com.example.lap8.ui.theme.Lap8Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lap8Theme {
                MyNavigation()
            }
        }
    }
}