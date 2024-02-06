package com.example.textconverter.picFormgallery

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt



//@Composable
//fun ImageCropper(bitmap: Bitmap, onCropAreaChanged: (Bitmap) -> Unit) {
//    BoxWithConstraints {
//
//        var offsetX by remember { mutableStateOf(0f) }
//        var offsetY by remember { mutableStateOf(0f) }
//
//        val minSize = 60f
//        var width by remember { mutableStateOf(minSize) }
//        var height by remember { mutableStateOf(minSize) }
//
//        val widthInDp: Dp
//        val heightInDp: Dp
//
//        with(LocalDensity.current){
//            widthInDp = width.toDp()
//            heightInDp = height.toDp()
//        }
//
//        val imageWidth = constraints.maxWidth
//        val imageHeight =  constraints.maxHeight
//
//        BoxWithConstraints(
//            modifier = Modifier
//                .offset {
//                    IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
//                }
//                .size(widthInDp, heightInDp)
//        ) {
//            // move the overlay
//            Box(
//                Modifier
//                    .background(Color.Transparent.copy(0.6f))
//                    .fillMaxSize()
//                    .pointerInput(Unit) {
//                        detectDragGestures { change, dragAmount ->
//                            offsetX = (offsetX + dragAmount.x).coerceIn(
//                                0f,
//                                (imageWidth - width).coerceAtLeast(0f)
//                            )
//                            offsetY = (offsetY + dragAmount.y).coerceIn(
//                                0f,
//                                (imageHeight - height).coerceAtLeast(0f)
//                            )
//                        }
//                    }
//            )
//
//            // resize the overlay
//            Box(
//                Modifier
//                    .align(Alignment.BottomEnd)
//                    .background(Color.White)
//                    .height(20.dp)
//                    .width(20.dp)
//                    .pointerInput(Unit) {
//                        detectDragGestures { change, dragAmount ->
//                            width = (width + dragAmount.x).coerceIn(
//                                minSize,
//                                (imageWidth - offsetX).coerceAtLeast(minSize)
//                            )
//                            height = (height + dragAmount.y).coerceIn(
//                                minSize,
//                                (imageHeight - offsetY).coerceAtLeast(minSize)
//                            )
//                        }
//                    }
//            )
//        }
//
//        // Calculate the position of the overlay within the original bitmap
//        val overlayX = (offsetX / imageWidth * bitmap.width).toInt()
//        val overlayY = (offsetY / imageHeight * bitmap.height).toInt()
//
//        // Calculate the size of the overlay within the original bitmap
//        val overlayWidth = (width / imageWidth * bitmap.width).toInt()
//        val overlayHeight = (height / imageHeight * bitmap.height).toInt()
//
//        // Crop the bitmap based on the overlay's position and size
//        val croppedBitmap = remember(bitmap, overlayX, overlayY, overlayWidth, overlayHeight) {
//            Bitmap.createBitmap(bitmap, overlayX, overlayY, overlayWidth, overlayHeight)
//        }
//
//        // Pass the cropped bitmap to the callback
//        onCropAreaChanged(croppedBitmap)
//    }
//}
