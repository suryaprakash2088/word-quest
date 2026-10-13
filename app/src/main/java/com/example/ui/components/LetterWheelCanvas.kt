package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AmberGold
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.WheelLineColor
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

data class WheelNode(
    val index: Int,
    val letter: Char,
    val center: Offset,
    val radius: Float
)

@Composable
fun LetterWheelCanvas(
    letters: List<Char>,
    onWordChanged: (String) -> Unit,
    onWordCompleted: (String) -> Unit,
    onLetterAdded: (indexInPath: Int) -> Unit,
    onShuffleClicked: () -> Unit,
    shuffleTrigger: Int,
    modifier: Modifier = Modifier
) {
    val selectedIndices = remember { mutableStateListOf<Int>() }
    var currentTouchPos by remember { mutableStateOf<Offset?>(null) }
    var wheelNodes by remember { mutableStateOf<List<WheelNode>>(emptyList()) }

    // Rotation animation on shuffle
    val rotationAnim = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(shuffleTrigger) {
        if (shuffleTrigger > 0) {
            rotationAnim.animateTo(
                targetValue = rotationAnim.value + 360f,
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
            )
        }
    }

    Box(
        modifier = modifier
            .testTag("letter_wheel_container"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .testTag("letter_wheel_canvas")
                .pointerInput(letters, rotationAnim.value) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        var activePointerId: PointerId = down.id
                        var currentPoint = down.position
                        currentTouchPos = currentPoint

                        selectedIndices.clear()

                        // Check if touch down landed on a letter node
                        wheelNodes.forEach { node ->
                            if (hypot(currentPoint.x - node.center.x, currentPoint.y - node.center.y) <= node.radius * 1.3f) {
                                selectedIndices.add(node.index)
                                onLetterAdded(0)
                                onWordChanged(letters[node.index].toString())
                            }
                        }

                        // Track continuous drag
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == activePointerId } ?: break

                            if (!change.pressed) {
                                // Touch lifted
                                break
                            }

                            currentPoint = change.position
                            currentTouchPos = currentPoint

                            // Check collision with nodes
                            wheelNodes.forEach { node ->
                                val dist = hypot(currentPoint.x - node.center.x, currentPoint.y - node.center.y)
                                if (dist <= node.radius * 1.15f) {
                                    if (!selectedIndices.contains(node.index)) {
                                        selectedIndices.add(node.index)
                                        val constructed = selectedIndices.map { letters[it] }.joinToString("")
                                        onLetterAdded(selectedIndices.size - 1)
                                        onWordChanged(constructed)
                                    }
                                }
                            }
                        }

                        // Drag finished (Finger up)
                        if (selectedIndices.isNotEmpty()) {
                            val word = selectedIndices.map { letters[it] }.joinToString("")
                            onWordCompleted(word)
                        }

                        selectedIndices.clear()
                        currentTouchPos = null
                        onWordChanged("")
                    }
                }
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val center = Offset(canvasWidth / 2f, canvasHeight / 2f)
            val outerRadius = (minOf(canvasWidth, canvasHeight) / 2f) * 0.90f
            val nodeRadius = outerRadius * 0.22f
            val ringRadius = outerRadius - nodeRadius - 10f

            // Outer backdrop ring
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x331E293B),
                        Color(0x770F172A)
                    ),
                    center = center,
                    radius = outerRadius
                ),
                radius = outerRadius,
                center = center
            )

            // Circular track outline
            drawCircle(
                color = Color(0x3338BDF8),
                radius = ringRadius,
                center = center,
                style = Stroke(width = 3.dp.toPx())
            )

            // Compute positions of letter nodes around ring
            val angleStep = (2 * PI / letters.size).toFloat()
            val currentRotationRad = (rotationAnim.value * PI / 180f).toFloat()

            val computedNodes = letters.indices.map { i ->
                val angle = -PI.toFloat() / 2f + (i * angleStep) + currentRotationRad
                val nx = center.x + ringRadius * cos(angle)
                val ny = center.y + ringRadius * sin(angle)
                WheelNode(
                    index = i,
                    letter = letters[i],
                    center = Offset(nx, ny),
                    radius = nodeRadius
                )
            }
            wheelNodes = computedNodes

            // Draw connecting lines between selected nodes
            if (selectedIndices.isNotEmpty()) {
                val path = Path()
                val firstNode = computedNodes[selectedIndices.first()]
                path.moveTo(firstNode.center.x, firstNode.center.y)

                for (idx in 1 until selectedIndices.size) {
                    val node = computedNodes[selectedIndices[idx]]
                    path.lineTo(node.center.x, node.center.y)
                }

                // Connect to current touch position
                currentTouchPos?.let { touch ->
                    path.lineTo(touch.x, touch.y)
                }

                // Outer glowing line
                drawPath(
                    path = path,
                    color = WheelLineColor.copy(alpha = 0.4f),
                    style = Stroke(
                        width = 16.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // Core crisp vibrant line
                drawPath(
                    path = path,
                    color = AmberGold,
                    style = Stroke(
                        width = 7.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // Draw Letter Nodes
            computedNodes.forEach { node ->
                val isSelected = selectedIndices.contains(node.index)
                val currentRadius = if (isSelected) node.radius * 1.14f else node.radius

                // Node shadow / outer halo
                if (isSelected) {
                    drawCircle(
                        color = AmberGold.copy(alpha = 0.5f),
                        radius = currentRadius + 8.dp.toPx(),
                        center = node.center
                    )
                }

                // Node background circle
                drawCircle(
                    brush = if (isSelected) {
                        Brush.verticalGradient(
                            listOf(Color(0xFFFBBF24), AmberGold)
                        )
                    } else {
                        Brush.verticalGradient(
                            listOf(Color(0xFFFFFFFF), Color(0xFFF1F5F9))
                        )
                    },
                    radius = currentRadius,
                    center = node.center
                )

                // Node border
                drawCircle(
                    color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                    radius = currentRadius,
                    center = node.center,
                    style = Stroke(width = if (isSelected) 3.5.dp.toPx() else 2.dp.toPx())
                )

                // Draw letter text using Android NativeCanvas for crisp typography & bold weight
                val paint = android.graphics.Paint().apply {
                    color = if (isSelected) android.graphics.Color.WHITE else android.graphics.Color.parseColor("#0F172A")
                    textSize = currentRadius * 1.05f
                    textAlign = android.graphics.Paint.Align.CENTER
                    typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
                    isAntiAlias = true
                    if (isSelected) {
                        setShadowLayer(6f, 0f, 2f, android.graphics.Color.argb(120, 0, 0, 0))
                    }
                }

                val fontMetrics = paint.fontMetrics
                val textY = node.center.y - (fontMetrics.descent + fontMetrics.ascent) / 2f
                drawContext.canvas.nativeCanvas.drawText(
                    node.letter.toString(),
                    node.center.x,
                    textY,
                    paint
                )
            }
        }

        // Center Shuffle Button
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
            shadowElevation = 4.dp,
            modifier = Modifier
                .size(48.dp)
                .testTag("shuffle_button")
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        onShuffleClicked()
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = "Shuffle Letters",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
