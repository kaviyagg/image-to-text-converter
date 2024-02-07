package com.example.textconverter.cameraImage

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.textconverter.picFormgallery.ImageCropper
import com.google.android.datatransport.BuildConfig
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import java.util.jar.Manifest

@OptIn(ExperimentalCoilApi::class)
@Composable
fun AppContent(navController: NavController) {

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        context,
        "com.example.textconverter.provider",
        file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(android.net.Uri.EMPTY)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(key1 = "gallerysLaunchKey") {
        val permissionCheckResult =
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            cameraLauncher.launch(uri)
        } else {
            // Request a permission
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

//    Column(
//        Modifier
//            .fillMaxSize()
//            .padding(10.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Button(onClick = {
//            val permissionCheckResult =
//                ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
//            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
//                cameraLauncher.launch(uri)
//            } else {
//                // Request a permission
//                permissionLauncher.launch(android.Manifest.permission.CAMERA)
//            }
//        }) {
//            Text(text = "Capture Image From Camera")
//        }
//    }

        if (capturedImageUri.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .padding(16.dp, 8.dp),
                painter = rememberImagePainter(capturedImageUri),
                contentDescription = null
            )
        }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (capturedImageUri != null) {
            // Load the bitmap from the capturedImageUri
//            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(capturedImageUri))
            // Display the detected text below the image
//            TextRecognitionOnImages(
////                bitmap = bitmap,
//                modifier = Modifier.fillMaxSize()
//            )
        }


        Spacer(modifier = Modifier.height(1.dp))
    }

    }


@Composable
fun TextRecognitionOnImages(bitmap: Bitmap, modifier: Modifier) {
    val textRecognizerOptions = TextRecognizerOptions.DEFAULT_OPTIONS
    val textRecognizer = remember { TextRecognition.getClient(textRecognizerOptions) }
    var showCroppedImage by remember { mutableStateOf(false) }
    var croppedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var extractedText by remember { mutableStateOf("") }


    // Display the image with recognized text
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
//
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Set the height of the image
                    .background(Color.Red.copy(0.6f)) // Apply overlay to the Image
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds, // Fill the bounds of the container
                    modifier = Modifier.fillMaxSize() // Fill the available space
                )
                ImageCropper(bitmap){ onCropAreaChanged ->
                    croppedBitmap = onCropAreaChanged
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {

                        // Launch the gallery to select an image


                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "Go to camera",color= Color.White)
                }

                Spacer(modifier = Modifier.width(16.dp)) // Adjust the space between buttons as needed

                Button(
                    onClick = {
                        showCroppedImage = !showCroppedImage
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "Save",color= Color.White)
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )

            LaunchedEffect(croppedBitmap) {
                val image = croppedBitmap?.let { InputImage.fromBitmap(it, 0) }
                if (image != null) {
                    textRecognizer.process(image)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                extractedText = it.result?.text ?: ""
                            }
                        }
                }
            }
            if(showCroppedImage){
                Text(
                    text = extractedText,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    color = Color.Black,
                    fontSize = 18.sp
                )
            }


        }
    }
}


