package com.example.textconverter

import MLKitTextRecognition
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.textconverter.cameraImage.AppContent
import com.example.textconverter.cameraImage.lens
import com.example.textconverter.picFormgallery.PickImageFromGallery
import com.example.textconverter.ui.theme.TextConverterTheme

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST = 34
private fun foregroundPermissionApproved(context: Context): Boolean {
    return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
        context, Manifest.permission.CAMERA
    )
}

private fun requestForegroundPermission(context: Context) {
    val provideRationale = foregroundPermissionApproved(context)

    if (provideRationale) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.CAMERA), REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST
        )
    } else {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.CAMERA), REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST
        )
    }
}

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextConverterTheme {
                requestForegroundPermission(this@MainActivity)
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "mlKitTextRecognition") {
                    composable("mlKitTextRecognition") {
                        MLKitTextRecognition(navController = navController)
                    }
                    composable("imageRecognition") {
                        PickImageFromGallery(navController = navController)
                    }
                    composable("camera") {
                        AppContent(navController = navController)
                    }
                }
            }
        }
    }
}

