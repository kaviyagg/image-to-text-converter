package com.example.textconverter.picFormgallery


import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.layout.ContentScale

@Composable
fun PickImageFromGallery(navController: NavController) {
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            // Handle the selected image URI
            uri?.let {
                imageUri = it
                // Load and process the selected image
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }
            }
        }
    )

    // Use LaunchedEffect to launch the image selection when the composable is initially displayed
    LaunchedEffect(key1 = "galleryLaunchKey") {
        if (imageUri == null) {
            // Launch the gallery to select an image
            galleryLauncher.launch("image/*")
        }
    }



    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (imageUri != null) {
            bitmap.value?.let { btm ->
                // Display the detected text below the image
                TextRecognitionOnImage(
                    bitmap = btm,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }


        Spacer(modifier = Modifier.height(1.dp))
    }
}




@Composable
fun TextRecognitionOnImage(bitmap: Bitmap, modifier: Modifier) {
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
//                ImageCropper(bitmap)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {

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

//            Text(
//                text = extractedText,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState())
//                    .padding(20.dp),
//                color = Color.Black,
//                fontSize = 18.sp
//            )


            croppedBitmap?.let {
                Image(
                    bitmap =  it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .background(Color.Transparent.copy(0.6f))
                )
            }


        }
    }
}





