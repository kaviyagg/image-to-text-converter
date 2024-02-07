package com.example.textconverter.navigation

sealed class Screen(val route: String){
    object MLKitTextRecognition : Screen("mlKitTextRecognition")
    object PickImageFromGallery : Screen("imageRecognition")
    object AppContent:Screen("camera")
}

