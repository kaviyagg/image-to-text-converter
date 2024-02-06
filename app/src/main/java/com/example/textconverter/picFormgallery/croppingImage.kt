package com.example.textconverter.picFormgallery

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
@Composable
fun ImageCropperrse(
//    bitmap: Bitmap,
//    showCroppedImage: Boolean,
//    onCropped: (Bitmap) -> Unit // Callback to provide the cropped bitmap
) {

    BoxWithConstraints {
        val imageWidth = constraints.maxWidth.toFloat()
        val imageHeight = constraints.maxHeight.toFloat()
        var boxState by remember { mutableStateOf(BoxState()) }
        var resizeState by remember { mutableStateOf(ResizeState()) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, _, _ ->
                        boxState = boxState.copy(
                            offsetX = boxState.offsetX + pan.x,
                            offsetY = boxState.offsetY + pan.y
                        )
                    }
                }
        ) {
            ResizableBox(
                modifier = Modifier
                    .offset(x = boxState.offsetX.dp, y = boxState.offsetY.dp)
                    .size(resizeState.width, resizeState.height)
                    .graphicsLayer {
                        scaleX = resizeState.scaleX
                        scaleY = resizeState.scaleY
                    },
                resizeState = remember { mutableStateOf(resizeState) },
                onResize = { newWidth, newHeight ->
                    resizeState = resizeState.copy(
                        width = newWidth,
                        height = newHeight
                    )
                }
            )

            // Draggable corner handles
            DraggableCornerHandle { dx, dy ->
                resizeState = resizeState.copy(
                    width = (resizeState.width + dx.dp).coerceAtLeast(50.dp),
                    height = (resizeState.height + dy.dp).coerceAtLeast(50.dp)
                )
            }

            // Draw three more corner handles
            DraggableCornerHandle { dx, dy ->
                resizeState = resizeState.copy(
                    width = (resizeState.width + dx.dp).coerceAtLeast(50.dp),
                    height = (resizeState.height - dy.dp).coerceAtLeast(50.dp)
                )
            }

            DraggableCornerHandle { dx, dy ->
                resizeState = resizeState.copy(
                    width = (resizeState.width - dx.dp).coerceAtLeast(50.dp),
                    height = (resizeState.height + dy.dp).coerceAtLeast(50.dp)
                )
            }

            DraggableCornerHandle { dx, dy ->
                resizeState = resizeState.copy(
                    width = (resizeState.width - dx.dp).coerceAtLeast(50.dp),
                    height = (resizeState.height - dy.dp).coerceAtLeast(50.dp)
                )
            }
        }
    }
}




@Composable
fun DraggableCornerHandle(onDrag: (Float, Float) -> Unit) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(Color.Red, shape = RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, _, _ ->
                    onDrag(-pan.x, -pan.y)
                }
            }
    )
}

@Composable
fun ResizableBox(
    modifier: Modifier = Modifier,
    resizeState: MutableState<ResizeState>,
    onResize: (Dp, Dp) -> Unit
) {
    Box(
        modifier = modifier
            .background(color = Color.Transparent, shape = RoundedCornerShape(16.dp))
            .border(1.dp, Color.Black, shape = RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, _, _ ->
                    onResize(
                        (resizeState.value.width + pan.x.dp).coerceAtLeast(50.dp),
                        (resizeState.value.height + pan.y.dp).coerceAtLeast(50.dp)
                    )
                }
            }
    ){
    }
}

data class ResizeState(
    var scaleX: Float = 1f,
    var scaleY: Float = 1f,
    var width: Dp = 200.dp,
    var height: Dp = 200.dp
)

data class BoxState(
    var offsetX: Float = 0f,
    var offsetY: Float = 0f
)