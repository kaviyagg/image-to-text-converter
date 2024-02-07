//package com.example.textconverter.cameraImage
//
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.ImageDecoder
//import android.net.Uri
//import android.os.Build
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.Divider
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.core.content.ContextCompat
//import androidx.core.content.FileProvider
//import androidx.navigation.NavController
//import coil.compose.rememberImagePainter
//import com.canhub.cropper.CropImageContract
//import com.example.textconverter.R
//import com.example.textconverter.picFormgallery.ImageCropper
//import com.example.textconverter.picFormgallery.ImageCropperrse
//
//import com.google.mlkit.vision.common.InputImage
//import com.google.mlkit.vision.text.TextRecognition
//import com.google.mlkit.vision.text.latin.TextRecognizerOptions
//import java.io.File
//import java.text.SimpleDateFormat
//import java.util.Date
//
//@RequiresApi(Build.VERSION_CODES.P)
//@Composable
//fun lens(navController: NavController){
//
//    val context = LocalContext.current
//    val file = context.createImageFile()
//    val uri = FileProvider.getUriForFile(
//        context,
//        "com.example.textconverter.provider",
//        file
//    )
//    var capturedImageUri by remember {
//        mutableStateOf<Uri>(Uri.EMPTY)
//    }
//
//    val cameraLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
//            capturedImageUri = uri
//        }
//
//    val permissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) {
//        if (it) {
//            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
//            cameraLauncher.launch(uri)
//        } else {
//            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    LaunchedEffect(key1 = "gallerysLaunchKey") {
//        val permissionCheckResult =
//            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
//        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
//            cameraLauncher.launch(uri)
//        } else {
//            // Request a permission
//            permissionLauncher.launch(Manifest.permission.CAMERA)
//        }
//    }
//    if (capturedImageUri.path?.isNotEmpty() == true) {
//
//        val source = ImageDecoder.createSource(context.contentResolver, capturedImageUri)
//        val bitmap = ImageDecoder.decodeBitmap(source)
//        TextRecognitionOnImages(
//            bitmap = bitmap,
//            modifier = Modifier
//                .fillMaxSize()
//        )
//
//    }
//}
//fun Context.createImageFile(): File {
//    // Create an image file name
//    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//    val imageFileName = "JPEG_" + timeStamp + "_"
//    val image = File.createTempFile(
//        imageFileName, /* prefix */
//        ".jpg", /* suffix */
//        externalCacheDir      /* directory */
//    )
//    return image
//}
//@Composable
//fun TextRecognitionOnImages(bitmap: Bitmap, modifier: Modifier) {
//    val textRecognizerOptions = TextRecognizerOptions.DEFAULT_OPTIONS
//    val textRecognizer = remember { TextRecognition.getClient(textRecognizerOptions) }
//    var croppedBitmap by remember { mutableStateOf<Bitmap?>(null) }
//    var extractedText by remember { mutableStateOf("") }
//
//    // Display the image with recognized text
//    Box(
//        modifier = modifier.fillMaxSize(),
//        contentAlignment = Alignment.TopStart
//    ) {
//        // Use a Column to arrange the image and text vertically
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//
//            Box(
//                modifier = Modifier.size(400.dp)
//            ) {
//                Image(
//                    bitmap = bitmap.asImageBitmap(),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(400.dp)
//                        .background(Color.Transparent.copy(0.6f)) // Apply overlay to the Image
//                )
//
//
////                 Move ImageCropper inside this Box
//                ImageCropper(bitmap){ onCropAreaChanged ->
//                    croppedBitmap = onCropAreaChanged
//                }
//
//            }
//
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Button(
//                    onClick = {
//
//                    },
//                    modifier = Modifier
//                        .weight(1f)
//                ) {
//                    Text(text = "Go to Camera")
//                }
//
//                Spacer(modifier = Modifier.width(16.dp)) // Adjust the space between buttons as needed
//
//                Button(
//                    onClick = {
//
//                    },
//                    modifier = Modifier
//                        .weight(1f)
//                ) {
//                    Text(text = "Save")
//                }
//            }
//
//            LaunchedEffect(croppedBitmap) {
//                val image = croppedBitmap?.let { InputImage.fromBitmap(it, 0) }
//                if (image != null) {
//                    textRecognizer.process(image)
//                        .addOnCompleteListener {
//                            if (it.isSuccessful) {
//                                extractedText = it.result?.text ?: ""
//                            }
//                        }
//                }
//            }
//
//
//            // Text at the bottom
//            Text(
//                text = extractedText,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(20.dp)
//                    .verticalScroll(rememberScrollState()),
//                color = Color.Black,
//                fontSize = 18.sp
//            )
//        }
//    }
//}
//
//
//
//
