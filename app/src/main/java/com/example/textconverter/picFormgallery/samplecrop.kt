package com.example.textconverter.picFormgallery

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

@Composable
internal fun DrawingOverlay(
    modifier: Modifier,
    drawOverlay: Boolean,
    rect: Rect,
    drawGrid: Boolean,
    transparentColor: Color,
    overlayColor: Color,
    handleColor: Color,
    strokeWidth: Dp,
    onRectChanged: (Rect) -> Unit,
    onDrawGrid: (DrawScope.(rect: Rect, strokeWidth: Float, color: Color) -> Unit)? = null,
) {
    val density = LocalDensity.current
    val layoutDirection: LayoutDirection = LocalLayoutDirection.current

    val strokeWidthPx = LocalDensity.current.run { strokeWidth.toPx() }

    val pathHandles = remember {
        Path()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, _, _ ->
                    // Handle moving the overlay
                    if (pan != Offset.Zero) {


                    }
                }
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    // Draw the overlay
                    drawRect(color = transparentColor)
                    drawRect(
                        color = overlayColor,
                        topLeft = rect.topLeft,
                        size = rect.size,
                        style = Stroke(width = strokeWidthPx)
                    )

                }
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, _, _ ->
                        // Check if the user is trying to resize the overlay
                        if (pan != Offset.Zero) {
                            pathHandles.apply {
                                reset()
                                updateHandlePath(rect, strokeWidthPx)
                            }

                            if (isPanOnHandle(pan, pathHandles, handleColor)) {
                                // Resize the overlay
                                val newRect = rect.resize(pan.x, pan.y)
                                onRectChanged(newRect)
                            }
                        }
                    }
                }
        ) {
            // Draw the grid if needed
            onDrawGrid?.invoke(this, rect, strokeWidthPx, overlayColor)
        }
    }
}



private fun isPanOnHandle(pan: Offset, path: Path, handleColor: Color): Boolean {
    val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)

    // Draw the handle path on a canvas


    // Check if the handle color is present at the pan position
    return bitmap.getPixel(0, 0) == handleColor.toArgb()
}

private fun Rect.resize(panX: Float, panY: Float): Rect {
    return copy(
        right = (right + panX).coerceIn(0f, Float.MAX_VALUE),
        bottom = (bottom + panY).coerceIn(0f, Float.MAX_VALUE)
    )
}

private fun Path.updateHandlePath(rect: Rect, strokeWidth: Float) {
    // Calculate handle size based on strokeWidth
    val handleSize = strokeWidth * 2

    if (rect != Rect.Zero) {
        // Top left lines
        moveTo(rect.left, rect.top + handleSize)
        lineTo(rect.left, rect.top)
        lineTo(rect.left + handleSize, rect.top)

        // Top right lines
        moveTo(rect.right - handleSize, rect.top)
        lineTo(rect.right, rect.top)
        lineTo(rect.right, rect.top + handleSize)

        // Bottom right lines
        moveTo(rect.right, rect.bottom - handleSize)
        lineTo(rect.right, rect.bottom)
        lineTo(rect.right - handleSize, rect.bottom)

        // Bottom left lines
        moveTo(rect.left + handleSize, rect.bottom)
        lineTo(rect.left, rect.bottom)
        lineTo(rect.left, rect.bottom - handleSize)
    }
}