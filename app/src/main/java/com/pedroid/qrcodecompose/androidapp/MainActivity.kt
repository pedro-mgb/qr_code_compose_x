package com.pedroid.qrcodecompose.androidapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.pedroid.qrcodecompose.androidapp.core.presentation.EdgeToEdgeEffect
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.QRCodeComposeCameraXTheme
import com.pedroid.qrcodecompose.androidapp.home.presentation.QRCodeApp
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // darkTheme=true because app currently only supports dark theme.
            EdgeToEdgeEffect(darkTheme = true)
            QRCodeComposeCameraXTheme {
                QRCodeApp(
                    windowSizeClass = calculateWindowSizeClass(this),
                )
            }
        }
    }
}
