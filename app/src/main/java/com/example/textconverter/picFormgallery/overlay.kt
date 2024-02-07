package com.example.textconverter.picFormgallery

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.textconverter.R
import kotlin.math.roundToInt

@Composable
fun ImageCropper(bitmap: Bitmap, onCropAreaChanged: (Bitmap) -> Unit) {
    BoxWithConstraints {

        var offsetX by remember { mutableStateOf(300f) }
        var offsetY by remember { mutableStateOf(300f) }

        val minSize =  30f
        var width by remember { mutableStateOf(400f) }
        var height by remember { mutableStateOf(300F) }

        val widthInDp: Dp
        val heightInDp: Dp

        with(LocalDensity.current){
            widthInDp = width.toDp()
            heightInDp = height.toDp()
        }

        val imageWidth = constraints.maxWidth
        val imageHeight =  constraints.maxHeight

        BoxWithConstraints(
            modifier = Modifier
                .offset {
                    IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
                }
                .size(widthInDp, heightInDp)
        ) {
            // move the overlay
            Box(
                Modifier
//                    .background(Color.Transparent)
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            offsetX = (offsetX + dragAmount.x).coerceIn(
                                0f,
                                (imageWidth - width).coerceAtLeast(0f)
                            )
                            offsetY = (offsetY + dragAmount.y).coerceIn(
                                0f,
                                (imageHeight - height).coerceAtLeast(0f)
                            )
                        }
                    }
                    .border(
                        width = 1.dp, // Border width
                        color = Color.White, // Border color
                    )

            )

            // resize the overlay
            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .height(20.dp)
                    .width(20.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            width = (width + dragAmount.x).coerceIn(
                                minSize,
                                (imageWidth - offsetX).coerceAtLeast(minSize)
                            )
                            height = (height + dragAmount.y).coerceIn(
                                minSize,
                                (imageHeight - offsetY).coerceAtLeast(minSize)
                            )
                        }
                    }
                        .clip(CircleShape)
                    .background(Color.White.copy(alpha=0.5f))
            ){
                Icon(
                    painter = painterResource(R.drawable.pictogrammers_material_arrow_arrow_top_left_bottom_right),
                    contentDescription = "Arrow"
                )

            }
        }

        // Calculate the position of the overlay within the original bitmap
        val overlayX = (offsetX / imageWidth * bitmap.width).toInt()
        val overlayY = (offsetY / imageHeight * bitmap.height).toInt()

        // Calculate the size of the overlay within the original bitmap
        val overlayWidth = (width / imageWidth * bitmap.width).toInt()
        val overlayHeight = (height / imageHeight * bitmap.height).toInt()

        // Crop the bitmap based on the overlay's position and size
        val croppedBitmap = remember(bitmap, overlayX, overlayY, overlayWidth, overlayHeight) {
            Bitmap.createBitmap(bitmap, overlayX, overlayY, overlayWidth, overlayHeight)
        }

        // Pass the cropped bitmap to the callback
//        onCropAreaChanged(croppedBitmap)

            onCropAreaChanged(croppedBitmap)
            // Set showCroppedImage to false after sending the cropped image

        Box(
            modifier = Modifier.fillMaxSize()
                .drawBehind {
                    drawOverlayAndShadow(
                        offsetX,
                        offsetY,
                        width,
                        height,
                        imageWidth.toFloat(),
                        imageHeight.toFloat()
                    )
                }
        )
    }
}

private fun DrawScope.drawOverlayAndShadow(
    offsetX: Float,
    offsetY: Float,
    width: Float,
    height: Float,
    imageWidth: Float,
    imageHeight: Float
) {
    val shadowColor = Color.Gray.copy(alpha = 0.7f)

    // Draw shadow for the uncovered area above the overlay
    drawRect(
        color = shadowColor,
        topLeft = Offset(0f, 0f),
        size = Size(imageWidth, offsetY)
    )

    // Draw shadow for the uncovered area below the overlay
    drawRect(
        color = shadowColor,
        topLeft = Offset(0f, offsetY + height),
        size = Size(imageWidth, imageHeight - offsetY - height)
    )

    // Draw shadow for the uncovered area left to the overlay
    drawRect(
        color = shadowColor,
        topLeft = Offset(0f, offsetY),
        size = Size(offsetX, height)
    )

    // Draw shadow for the uncovered area right to the overlay
    drawRect(
        color = shadowColor,
        topLeft = Offset(offsetX + width, offsetY),
        size = Size(imageWidth - offsetX - width, height)
    )
}
