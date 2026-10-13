package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GridCell
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.WoodTileBg
import com.example.ui.theme.WoodTileBorder

@Composable
fun CrosswordGrid(
    gridCells: List<List<GridCell>>,
    recentlyFoundCoords: Set<String>,
    onCellClicked: (row: Int, col: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (gridCells.isEmpty()) return
    val rows = gridCells.size
    val cols = gridCells[0].size

    BoxWithConstraints(
        modifier = modifier
            .testTag("crossword_grid_container")
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        val maxAvailableWidth = maxWidth
        val maxAvailableHeight = maxHeight

        val cellSize = minOf(
            maxAvailableWidth / (cols + 0.4f),
            maxAvailableHeight / (rows + 0.4f)
        ).coerceIn(36.dp, 60.dp)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (r in 0 until rows) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (c in 0 until cols) {
                        val cell = gridCells[r][c]
                        CrosswordCellItem(
                            cell = cell,
                            cellSize = cellSize,
                            isRecentlyFound = recentlyFoundCoords.contains("$r,$c"),
                            onCellClicked = { onCellClicked(r, c) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CrosswordCellItem(
    cell: GridCell,
    cellSize: androidx.compose.ui.unit.Dp,
    isRecentlyFound: Boolean,
    onCellClicked: () -> Unit
) {
    if (!cell.isPartOfWord) {
        // Empty space in grid
        Box(
            modifier = Modifier
                .size(cellSize)
                .padding(2.dp)
        )
        return
    }

    val scaleAnim = remember { Animatable(1f) }

    LaunchedEffect(isRecentlyFound) {
        if (isRecentlyFound) {
            scaleAnim.animateTo(
                targetValue = 1.25f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            )
            scaleAnim.animateTo(1f)
        }
    }

    val isVisible = cell.isRevealed || cell.isHintRevealed
    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = Modifier
            .size(cellSize)
            .padding(2.5.dp)
            .scale(scaleAnim.value)
            .shadow(if (isVisible) 3.dp else 1.dp, shape)
            .clip(shape)
            .background(
                when {
                    isRecentlyFound -> EmeraldSuccess
                    cell.isHintRevealed && !cell.isRevealed -> AmberGold.copy(alpha = 0.9f)
                    cell.isRevealed -> MaterialTheme.colorScheme.primaryContainer
                    else -> WoodTileBg
                }
            )
            .border(
                width = if (isRecentlyFound) 2.dp else 1.dp,
                color = when {
                    isRecentlyFound -> EmeraldSuccess
                    cell.isRevealed -> MaterialTheme.colorScheme.primary
                    cell.isHintRevealed -> AmberGold
                    else -> WoodTileBorder
                },
                shape = shape
            )
            .clickable(enabled = !cell.isRevealed) { onCellClicked() }
            .testTag("cell_${cell.row}_${cell.col}"),
        contentAlignment = Alignment.Center
    ) {
        if (isVisible) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + scaleIn()
            ) {
                Text(
                    text = cell.letter.toString(),
                    fontSize = (cellSize.value * 0.48f).sp,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        isRecentlyFound -> Color.White
                        cell.isHintRevealed && !cell.isRevealed -> Color.White
                        cell.isRevealed -> MaterialTheme.colorScheme.onPrimaryContainer
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}
