package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class Particle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val size: Float,
    val color: Color,
    val rotation: Float,
    val vRot: Float
)

@Composable
fun ConfettiOverlay(
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    if (!isActive) return

    val progress = remember { Animatable(0f) }
    val particles = remember {
        val colors = listOf(
            Color(0xFFF59E0B),
            Color(0xFF10B981),
            Color(0xFF0EA5E9),
            Color(0xFFEC4899),
            Color(0xFF8B5CF6),
            Color(0xFFEAB308)
        )
        List(65) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat() * 0.3f - 0.2f,
                vx = (Random.nextFloat() - 0.5f) * 0.5f,
                vy = Random.nextFloat() * 0.8f + 0.4f,
                size = Random.nextFloat() * 14f + 8f,
                color = colors.random(),
                rotation = Random.nextFloat() * 360f,
                vRot = (Random.nextFloat() - 0.5f) * 720f
            )
        }
    }

    LaunchedEffect(isActive) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2200, easing = LinearEasing)
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val t = progress.value
        val w = size.width
        val h = size.height

        particles.forEach { p ->
            val currX = (p.x + p.vx * t) * w
            val currY = (p.y + p.vy * t + 0.5f * 9.8f * t * t * 0.08f) * h
            val alpha = (1f - t * 0.8f).coerceIn(0f, 1f)

            if (currY in 0f..h && currX in 0f..w) {
                drawRect(
                    color = p.color.copy(alpha = alpha),
                    topLeft = Offset(currX, currY),
                    size = Size(p.size, p.size * 0.7f)
                )
            }
        }
    }
}
