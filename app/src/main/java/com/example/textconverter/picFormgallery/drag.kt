//package com.example.textconverter.picFormgallery
//
//
//import android.graphics.Bitmap
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.detectTransformGestures
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxWithConstraints
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.drawWithContent
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.geometry.times
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.PointerEventType
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.unit.IntOffset
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.times
//import kotlin.math.roundToInt
//import kotlin.math.absoluteValue
//@Composable
//fun ImageCropperrse(
//    bitmap: Bitmap,
//    showCroppedImage: Boolean,
//    onCropped: (Bitmap) -> Unit // Callback to provide the cropped bitmap
//) {
//    var offsetX by remember { mutableStateOf(40f) }
//    var offsetY by remember { mutableStateOf(40f) }
//    var width by remember { mutableStateOf(400f) }
//    var height by remember { mutableStateOf(400f) }
//    var resizing by remember { mutableStateOf(false) }
//    var zooming by remember { mutableStateOf(false) }
//    val zoomThreshold = 1.1f
//
//
//    BoxWithConstraints {
//        val imageWidth = constraints.maxWidth.toFloat()
//        val imageHeight = constraints.maxHeight.toFloat()
//
//        Box(
//            modifier = Modifier
//                .offset {
//                    IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
//                }
//                .size(width.dp, height.dp)
//                .background(
//                    color = Color.Transparent,
//                    shape = RoundedCornerShape(0.dp)
//                )
//                .pointerInput(Unit) {
//                    detectTransformGestures { _, pan, zoom, _ ->
//                        if (pan != Offset.Zero) {
//                            if (!zooming) {
//                                // Move the overlay by dragging the inner content
//                                offsetX += pan.x
//                                offsetY += pan.y
//                                // Ensure the offset is within bounds
//                                offsetX = offsetX.coerceIn(0f, imageWidth - width)
//                                offsetY = offsetY.coerceIn(0f, imageHeight - height)
//                            }
//                        }
//                        if (zoom != 1f) {
//                            if (zoom > zoomThreshold) {
//                                // Zooming is happening
//                                zooming = true
//                            } else {
//                                zooming = false
//                            }
//                        }
//                    }
//                }
//
//
//
//        )
//
//        // Calculate the dimensions of the full image covered by the overlay
//        val overlayX = (offsetX * (bitmap.width.toFloat() / imageWidth)).roundToInt()
//        val overlayY = (offsetY * (bitmap.height.toFloat() / imageHeight)).roundToInt()
//        val overlayWidth = (width * (bitmap.width.toFloat() / imageWidth)).roundToInt()
//        val overlayHeight = (height * (bitmap.height.toFloat() / imageHeight)).roundToInt()
//
//        // Ensure that the calculated values are within bounds
//        val finalX = overlayX.coerceIn(0, bitmap.width - 1)
//        val finalY = overlayY.coerceIn(0, bitmap.height - 1)
//        val finalWidth = (overlayWidth + finalX).coerceIn(finalX + 1, bitmap.width)
//        val finalHeight = (overlayHeight + finalY).coerceIn(finalY, bitmap.height)
//
//        // Crop the full image covered by the overlay
//        val croppedBitmap = Bitmap.createBitmap(
//            bitmap,
//            finalX,
//            finalY,
//            finalWidth - finalX,
//            finalHeight - finalY
//        )
//
//        // Pass the cropped bitmap to the callback
//        onCropped(croppedBitmap)
//
//        // Draw overlay
//        DrawImageOverlayse(
//            offsetX = offsetX,
//            offsetY = offsetY,
//            width = width,
//            height = height,
//            imageWidth = imageWidth,
//            imageHeight = imageHeight,
//            resizing = resizing,
//            onResize = { newWidth, newHeight ->
//                // Handle the resizing logic here
//                // You can update the width and height based on the new values
//                width = newWidth
//                height = newHeight
//            }
//        )
//    }
//}
//
//
//@Composable
//fun DrawImageOverlayse(
//    offsetX: Float,
//    offsetY: Float,
//    width: Float,
//    height: Float,
//    imageWidth: Float,
//    imageHeight: Float,
//    resizing: Boolean,
//    onResize: (Float, Float) -> Unit
//) {
//    val cornerSize = 5.dp
//    val cornerBorderColor = Color.Black
//    val cornerBorderWidth = 2.dp
//    val shadowColor = Color.Gray.copy(alpha = 0.5f)
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//
//        // Draw shadow for the uncovered area
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .drawWithContent {
//                    drawContent()
//
//                    // Draw the uncovered area
//                    drawRect(
//                        color = shadowColor,
//                        topLeft = Offset(0f, 0f),
//                        size = Size(imageWidth, offsetY)
//                    )
//                    drawRect(
//                        color = shadowColor,
//                        topLeft = Offset(0f, offsetY),
//                        size = Size(offsetX, height)
//                    )
//                    drawRect(
//                        color = shadowColor,
//                        topLeft = Offset(offsetX + width, offsetY),
//                        size = Size(imageWidth - offsetX - width, height)
//                    )
//                    drawRect(
//                        color = shadowColor,
//                        topLeft = Offset(0f, offsetY + height),
//                        size = Size(imageWidth, imageHeight - offsetY - height)
//                    )
//                }
//        )
//
//        // Draw black border around the entire overlay
//        Box(
//            modifier = Modifier
//                .offset {
//                    IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
//                }
//                .size(width.dp, height.dp)
//                .background(
//                    color = Color.Transparent,
//                    shape = RoundedCornerShape(0.dp)
//                )
//                .drawWithContent {
//                    drawContent()
//
//                    // Draw top border
//                    drawRect(
//                        color = cornerBorderColor,
//                        topLeft = Offset(0f, 0f),
//                        size = Size(width, cornerBorderWidth.toPx())
//                    )
//
//                    // Draw left border
//                    drawRect(
//                        color = cornerBorderColor,
//                        topLeft = Offset(0f, 0f),
//                        size = Size(cornerBorderWidth.toPx(), height)
//                    )
//
//                    // Draw right border
//                    drawRect(
//                        color = cornerBorderColor,
//                        topLeft = Offset(width - cornerBorderWidth.toPx(), 0f),
//                        size = Size(cornerBorderWidth.toPx(), height)
//                    )
//
//                    // Draw bottom border
//                    drawRect(
//                        color = cornerBorderColor,
//                        topLeft = Offset(0f, height - cornerBorderWidth.toPx()),
//                        size = Size(width, cornerBorderWidth.toPx())
//                    )
//                }
//        )
//
//        // Draw top left corner for resizing
//        Box(
//            modifier = Modifier
//                .size(cornerSize)
//                .offset {
//                    IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
//                }
//                .pointerInput(Unit) {
//                    detectTransformGestures { _, pan, zoom, _ ->
//                        if (zoom == 1f) {
//                            if (pan.x.absoluteValue < cornerSize.toPx() && pan.y.absoluteValue < cornerSize.toPx()) {
//                                // Resizing is happening from the top left corner
//                                onResize(width - pan.x, height - pan.y)
//                            }
//                        }
//                    }
//                }
//                .drawWithContent {
//                    drawContent()
//                    drawLine(
//                        color = cornerBorderColor,
//                        start = Offset(0f, 0f),
//                        end = Offset(cornerSize.toPx(), 0f),
//                        strokeWidth = cornerBorderWidth.toPx()
//                    )
//                    drawLine(
//                        color = cornerBorderColor,
//                        start = Offset(0f, 0f),
//                        end = Offset(0f, cornerSize.toPx()),
//                        strokeWidth = cornerBorderWidth.toPx()
//                    )
//                }
//        )
//
//        // Draw top right corner for resizing
//        Box(
//            modifier = Modifier
//                .size(cornerSize)
//                .offset {
//                    IntOffset((offsetX + width - cornerSize.toPx()).roundToInt(), offsetY.roundToInt())
//                }
//                .pointerInput(Unit) {
//                    detectTransformGestures { _, pan, zoom, _ ->
//                        if (zoom == 1f) {
//                            if ((width - pan.x).absoluteValue < cornerSize.toPx() && pan.y.absoluteValue < cornerSize.toPx()) {
//                                // Resizing is happening from the top right corner
//                                onResize(width + pan.x, height - pan.y)
//                            }
//                        }
//                    }
//                }
//                .drawWithContent {
//                    drawContent()
//                    drawLine(
//                        color = cornerBorderColor,
//                        start = Offset(0f, 0f),
//                        end = Offset(cornerSize.toPx(), 0f),
//                        strokeWidth = cornerBorderWidth.toPx()
//                    )
//                    drawLine(
//                        color = cornerBorderColor,
//                        start = Offset(cornerSize.toPx(), 0f),
//                        end = Offset(cornerSize.toPx(), cornerSize.toPx()),
//                        strokeWidth = cornerBorderWidth.toPx()
//                    )
//                }
//        )
//
//        // Draw bottom left corner for resizing
//        Box(
//            modifier = Modifier
//                .size(cornerSize)
//                .offset {
//                    IntOffset(offsetX.roundToInt(), (offsetY + height - cornerSize.toPx()).roundToInt())
//                }
//                .pointerInput(Unit) {
//                    detectTransformGestures { _, pan, zoom, _ ->
//                        if (zoom == 1f) {
//                            if (pan.x.absoluteValue < cornerSize.toPx() && (height - pan.y).absoluteValue < cornerSize.toPx()) {
//                                // Resizing is happening from the bottom left corner
//                                onResize(width - pan.x, height + pan.y)
//                            }
//                        }
//                    }
//                }
//                .drawWithContent {
//                    drawContent()
//                    drawLine(
//                        color = cornerBorderColor,
//                        start = Offset(0f, cornerSize.toPx()),
//                        end = Offset(cornerSize.toPx(), cornerSize.toPx()),
//                        strokeWidth = cornerBorderWidth.toPx()
//                    )
//                    drawLine(
//                        color = cornerBorderColor,
//                        start = Offset(0f, cornerSize.toPx()),
//                        end = Offset(0f, 0f),
//                        strokeWidth = cornerBorderWidth.toPx()
//                    )
//                }
//        )
//
//        // Draw bottom right corner for resizing
//        Box(
//            modifier = Modifier
//                .size(cornerSize)
//                .offset {
//                    IntOffset(
//                        (offsetX + width - cornerSize.toPx()).roundToInt(),
//                        (offsetY + height - cornerSize.toPx()).roundToInt()
//                    )
//                }
//                .pointerInput(Unit) {
//                    detectTransformGestures { _, pan, zoom, _ ->
//                        if (zoom == 1f) {
//                            if ((width - pan.x).absoluteValue < cornerSize.toPx() && (height - pan.y).absoluteValue < cornerSize.toPx()) {
//                                // Resizing is happening from the bottom right corner
//                                onResize(width + pan.x, height + pan.y)
//                            }
//                        }
//                    }
//                }
//                .drawWithContent {
//                    drawContent()
//                    drawLine(
//                        color = cornerBorderColor,
//                        start = Offset(cornerSize.toPx(), cornerSize.toPx()),
//                        end = Offset(0f, cornerSize.toPx()),
//                        strokeWidth = cornerBorderWidth.toPx()
//                    )
//                    drawLine(
//                        color = cornerBorderColor,
//                        start = Offset(cornerSize.toPx(), cornerSize.toPx()),
//                        end = Offset(cornerSize.toPx(), 0f),
//                        strokeWidth = cornerBorderWidth.toPx()
//                    )
//                }
//        )
//    }
//}