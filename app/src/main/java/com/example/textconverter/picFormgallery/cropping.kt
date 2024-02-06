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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

//@Composable
//fun ImageCropper(
//    bitmap: Bitmap,
//    onCropped: (Bitmap) -> Unit
//) {
//    var offsetX by remember { mutableStateOf(0f) }
//    var offsetY by remember { mutableStateOf(0f) }
//
//    var width by remember { mutableStateOf(100f) }
//    var height by remember { mutableStateOf(100f) }
//
//    val minSize = 100f
//
//    val widthInDp: Dp
//    val heightInDp: Dp
//
//    with(LocalDensity.current) {
//        widthInDp = width.toDp()
//        heightInDp = height.toDp()
//    }
//
//    val imageWidth = 400.dp // Set your desired image width
//    val imageHeight = 300.dp // Set your desired image height
//
//    // Overlay with grey color for non-covered area
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .drawWithContent {
//                drawContent()
//                drawOverlayAndShadow(offsetX, offsetY, width, height, imageWidth.toPx(), imageHeight.toPx())
//            }
//            .pointerInput(Unit) {
//                detectDragGestures { _, dragAmount ->
//                    offsetX = (offsetX + dragAmount.x).coerceIn(
//                        0f,
//                        (imageWidth.toPx() - width).coerceAtLeast(0f)
//                    )
//                    offsetY = (offsetY + dragAmount.y).coerceIn(
//                        0f,
//                        (imageHeight.toPx() - height).coerceAtLeast(0f)
//                    )
//                }
//            }
//
//    ) {
//        Box(
//            modifier = Modifier
//                .offset {
//                    IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
//                }
//                .size(widthInDp, heightInDp)
//        ) {
//            // Resize controls
//            Box(
//                Modifier
//                    .align(Alignment.BottomEnd)
//                    .height(20.dp)
//                    .width(20.dp)
//                    .pointerInput(Unit) {
//                        detectDragGestures { _, dragAmount ->
//                            width = (width + dragAmount.x).coerceIn(
//                                minSize,
//                                (imageWidth.toPx() - offsetX).coerceAtLeast(minSize)
//                            )
//                            height = (height + dragAmount.y).coerceIn(
//                                minSize,
//                                (imageHeight.toPx() - offsetY).coerceAtLeast(minSize)
//                            )
//                        }
//                    }
//                    .clip(CircleShape)
//                    .background(Color.Red)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.ArrowForward,
//                    contentDescription = null,
//                    modifier = Modifier.fillMaxSize(),
//                    tint = Color.White
//                )
//            }
//        }
//
//        // Adjusted onCropped function to consider only the overlay-covered area
//        val croppedBitmap = cropBitmap(bitmap, offsetX, offsetY, width, height, imageWidth.value, imageHeight.value)
//        onCropped(croppedBitmap)
//    }
//}
//
//private fun cropBitmap(
//    original: Bitmap,
//    offsetX: Float,
//    offsetY: Float,
//    width: Float,
//    height: Float,
//    imageWidth: Float,
//    imageHeight: Float
//): Bitmap {
//    // Calculate the crop bounds based on overlay position and size
//    val cropX = (offsetX / imageWidth * original.width).coerceIn(0f, original.width.toFloat())
//    val cropY = (offsetY / imageHeight * original.height).coerceIn(0f, original.height.toFloat())
//    val cropWidth = (width / imageWidth * original.width).coerceIn(0f, original.width - cropX)
//    val cropHeight = (height / imageHeight * original.height).coerceIn(0f, original.height - cropY)
//    println(cropX )
//    println(cropY)
//    println(cropWidth)
//    println(cropHeight)
//
//
//    // Convert to integers without extra padding
//    val cropXInt = cropX.toInt()
//    val cropYInt = cropY.toInt()
//    val cropWidthInt = cropWidth.toInt()
//    val cropHeightInt = cropHeight.toInt()
//
//    // Create a new Bitmap using the calculated crop bounds
//    return Bitmap.createBitmap(original, cropXInt, cropYInt, cropWidthInt, cropHeightInt)
//}
//private fun DrawScope.drawOverlayAndShadow(
//    offsetX: Float,
//    offsetY: Float,
//    width: Float,
//    height: Float,
//    imageWidth: Float,
//    imageHeight: Float
//) {
//    val shadowColor = Color.Gray.copy(alpha = 0.5f)
//
//    // Draw shadow for the uncovered area
//    drawRect(
//        color = shadowColor,
//        topLeft = Offset(0f, 0f),
//        size = Size(imageWidth, offsetY)
//    )
//    drawRect(
//        color = shadowColor,
//        topLeft = Offset(0f, offsetY),
//        size = Size(offsetX, height)
//    )
//    drawRect(
//        color = shadowColor,
//        topLeft = Offset(offsetX + width, offsetY),
//        size = Size(imageWidth - offsetX - width, height)
//    )
//    drawRect(
//        color = shadowColor,
//        topLeft = Offset(0f, offsetY + height),
//        size = Size(imageWidth, imageHeight - offsetY - height)
//    )
//}
