package com.example.textconverter.cameraImage

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.example.textconverter.picFormgallery.ImageCropperrse
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun lense(navController: NavController){
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        context,
        "com.example.textconverter.provider",
        file
    )
    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cropLauncher =
        rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
            if (result.isSuccessful) {
                result.uriContent?.let {
                    // Crop successful, update the imageUri
                    imageUri = it
                }
            } else {
                // Handle crop error
                println("ImageCropping error: ${result.error}")
            }
        }

    // Use LaunchedEffect to launch the image selection when the composable is initially displayed
    LaunchedEffect(key1 = "galleryLaunchKey") {
        if(imageUri === null){
            val cropOptions = CropImageContractOptions(
                null, // Pass URI here if available
                CropImageOptions(
                    cropShape = CropImageView.CropShape.RECTANGLE
                    // Add any other options you need for cropping
                )
            )
            cropLauncher.launch(cropOptions)
        }
    }



    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Check if imageUri is not null
        if (imageUri != null) {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, imageUri!!)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }
        }
                // Display the detected text below the image
                TextRecognitionOnImage(
                    bitmap =  bitmap,
                    modifier = Modifier
                        .fillMaxSize(),
                    cropLauncher = cropLauncher
                )

        }

        Spacer(modifier = Modifier.height(1.dp))
    }


@Composable
fun TextRecognitionOnImage(bitmap: MutableState<Bitmap?>, modifier: Modifier,cropLauncher: ActivityResultLauncher<CropImageContractOptions>) {
    val navController = rememberNavController()
    val textRecognizerOptions = TextRecognizerOptions.DEFAULT_OPTIONS
    val textRecognizer = remember { TextRecognition.getClient(textRecognizerOptions) }
    var showCroppedImage by remember { mutableStateOf(false) }
    var croppedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val extractedText = remember { mutableStateOf("") }
    // Process the bitmap using ML Kit Text Recognition
     val image = croppedBitmap?.let { InputImage.fromBitmap(it, 0) }
        if (image != null) {
            textRecognizer.process(image)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        extractedText.value = it.result?.text ?: ""
                    }
                }
        }

//    bitmap.value?.let {
//        ImageCropperrse(it, showCroppedImage) { cropped ->
//            croppedBitmap = cropped
//        }
//    }

    // Display the image with recognized text
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        // Use a Column to arrange the image and text vertically

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Image at the top
            bitmap.value?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier
                        .height(400.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        cropLauncher.launch(null)
                    },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "Go to Camera")
                }

                Spacer(modifier = Modifier.width(16.dp)) // Adjust the space between buttons as needed

                Button(
                    onClick = {
                        showCroppedImage = !showCroppedImage
                    },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "Save")
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )

            // Text at the bottom
            Text(
                text = extractedText.value,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                color = Color.Black,
                fontSize = 18.sp
            )
        }
    }
}


