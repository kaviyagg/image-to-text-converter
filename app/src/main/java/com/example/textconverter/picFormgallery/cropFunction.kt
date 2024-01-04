package com.example.textconverter.picFormgallery

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


@Composable
fun ImageCropperrse(
    bitmap: Bitmap,
    showCroppedImage: Boolean,
    onCropped: (Bitmap) -> Unit // Callback to provide the cropped bitmap
) {
    var offsetX by remember { mutableStateOf(40f) }
    var offsetY by remember { mutableStateOf(40f) }
    var width by remember { mutableStateOf(990f) }
    var height by remember { mutableStateOf(990f) }

    BoxWithConstraints {
        val imageWidth = constraints.maxWidth.toFloat()
        val imageHeight = constraints.maxHeight.toFloat()

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
                }
                .size(width.dp, height.dp)
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(0.dp)
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        // Adjust both width and height by the same zoom factor
                        width *= zoom
                        height *= zoom

                        // Adjust offsetX and offsetY based on the pan and zoom factors
                        offsetX += pan.x * width / imageWidth
                        offsetY += pan.y * height / imageHeight

                        // Ensure the width and height are within bounds
                        width = width.coerceIn(50f, imageWidth.toFloat())
                        height = height.coerceIn(50f, imageHeight.toFloat())

                        // Ensure offsetX and offsetY are within bounds
                        offsetX = offsetX.coerceIn(0f, imageWidth - width)
                        offsetY = offsetY.coerceIn(0f, imageHeight - height)
                    }
                }
        )


            // Calculate the dimensions of the full image covered by the overlay
            val overlayX = (offsetX * (bitmap.width.toFloat() / imageWidth)).roundToInt()
            val overlayY = (offsetY * (bitmap.height.toFloat() / imageHeight)).roundToInt()
            val overlayWidth = (width * (bitmap.width.toFloat() / imageWidth)).roundToInt()
            val overlayHeight = (height * (bitmap.height.toFloat() / imageHeight)).roundToInt()

            // Ensure that the calculated values are within bounds
            val finalX = overlayX.coerceIn(0, bitmap.width - 1)
            val finalY = overlayY.coerceIn(0, bitmap.height - 1)
            val finalWidth = (overlayWidth + finalX).coerceIn(finalX + 1, bitmap.width)
            val finalHeight = (overlayHeight + finalY).coerceIn(finalY, bitmap.height)

            // Crop the full image covered by the overlay
            val croppedBitmap = Bitmap.createBitmap(
                bitmap,
                finalX,
                finalY,
                finalWidth - finalX,
                finalHeight - finalY
            )

            // Pass the cropped bitmap to the callback
            onCropped(croppedBitmap)

        // Draw overlay
        DrawImageOverlayse(
            offsetX = offsetX,
            offsetY = offsetY,
            width = width,
            height = height,
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )
    }
}


@Composable
fun DrawImageOverlayse(
    offsetX: Float,
    offsetY: Float,
    width: Float,
    height: Float,
    imageWidth: Float,
    imageHeight: Float
) {
    // ... (Previous code remains unchanged)
    val cornerSize = 5.dp
    val cornerBorderColor = Color.Black
    val cornerBorderWidth = 2.dp
    val shadowColor = Color.Gray.copy(alpha = 0.5f)

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            // Draw shadow for the uncovered area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawContent()

                        // Draw the uncovered area
                        drawRect(
                            color = shadowColor,
                            topLeft = Offset(0f, 0f),
                            size = Size(imageWidth, offsetY)
                        )
                        drawRect(
                            color = shadowColor,
                            topLeft = Offset(0f, offsetY),
                            size = Size(offsetX, height)
                        )
                        drawRect(
                            color = shadowColor,
                            topLeft = Offset(offsetX + width, offsetY),
                            size = Size(imageWidth - offsetX - width, height)
                        )
                        drawRect(
                            color = shadowColor,
                            topLeft = Offset(0f, offsetY + height),
                            size = Size(imageWidth, imageHeight - offsetY - height)
                        )
                    }
            )




            // Draw top left corner border
            Box(
                modifier = Modifier
                    .size(cornerSize)
                    .offset {
                        IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
                    }
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = cornerBorderColor,
                            start = Offset(0f, 0f),
                            end = Offset(cornerSize.toPx(), 0f),
                            strokeWidth = cornerBorderWidth.toPx()
                        )
                        drawLine(
                            color = cornerBorderColor,
                            start = Offset(0f, 0f),
                            end = Offset(0f, cornerSize.toPx()),
                            strokeWidth = cornerBorderWidth.toPx()
                        )
                    }
            )

            // Draw top right corner border
            Box(
                modifier = Modifier
                    .size(cornerSize)
                    .offset {
                        IntOffset((offsetX + width - cornerSize.toPx()).roundToInt(), offsetY.roundToInt())
                    }
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = cornerBorderColor,
                            start = Offset(cornerSize.toPx(), 0f),
                            end = Offset(0f, 0f),
                            strokeWidth = cornerBorderWidth.toPx()
                        )
                        drawLine(
                            color = cornerBorderColor,
                            start = Offset(cornerSize.toPx(), 0f),
                            end = Offset(cornerSize.toPx(), cornerSize.toPx()),
                            strokeWidth = cornerBorderWidth.toPx()
                        )
                    }
            )

            // Draw bottom left corner border
            Box(
                modifier = Modifier
                    .size(cornerSize)
                    .offset {
                        IntOffset(offsetX.roundToInt(), (offsetY + height - cornerSize.toPx()).roundToInt())
                    }
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = cornerBorderColor,
                            start = Offset(0f, cornerSize.toPx()),
                            end = Offset(cornerSize.toPx(), cornerSize.toPx()),
                            strokeWidth = cornerBorderWidth.toPx()
                        )
                        drawLine(
                            color = cornerBorderColor,
                            start = Offset(0f, cornerSize.toPx()),
                            end = Offset(0f, 0f),
                            strokeWidth = cornerBorderWidth.toPx()
                        )
                    }
            )

            // Draw bottom right corner border
            Box(
                modifier = Modifier
                    .size(cornerSize)
                    .offset {
                        IntOffset(
                            (offsetX + width - cornerSize.toPx()).roundToInt(),
                            (offsetY + height - cornerSize.toPx()).roundToInt()
                        )
                    }
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = cornerBorderColor,
                            start = Offset(cornerSize.toPx(), cornerSize.toPx()),
                            end = Offset(0f, cornerSize.toPx()),
                            strokeWidth = cornerBorderWidth.toPx()
                        )
                        drawLine(
                            color = cornerBorderColor,
                            start = Offset(cornerSize.toPx(), cornerSize.toPx()),
                            end = Offset(cornerSize.toPx(), 0f),
                            strokeWidth = cornerBorderWidth.toPx()
                        )
                    }
            )
        }
    }

