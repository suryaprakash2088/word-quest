package com.example.ui.gameplay

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.levels.LevelRepository
import com.example.ui.components.AdSimulationDialog
import com.example.ui.components.ConfettiOverlay
import com.example.ui.components.CrosswordGrid
import com.example.ui.components.LetterWheelCanvas
import com.example.ui.components.StoreDialog
import com.example.ui.components.WordPreviewBar
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.TealPrimary

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToLevels: () -> Unit,
    onNavigateToDictionary: (initialWord: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val syncState by viewModel.syncState.collectAsState()

    BackHandler {
        if (uiState.isPaused) {
            viewModel.setPaused(false)
        } else {
            viewModel.setPaused(true)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("game_screen")
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isTabletWide = maxWidth >= 600.dp

            if (isTabletWide) {
                // Wide / Tablet side-by-side layout
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left pane: Header + Crossword Grid
                    Column(
                        modifier = Modifier
                            .weight(1.1f)
                            .fillMaxHeight()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GameTopHeader(
                            uiState = uiState,
                            onPauseClicked = { viewModel.setPaused(true) },
                            onCoinsClicked = { viewModel.setStoreDialogVisible(true) },
                            onDictionaryClicked = { onNavigateToDictionary(null) }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        CrosswordGrid(
                            gridCells = uiState.gridCells,
                            recentlyFoundCoords = uiState.recentlyFoundCoords,
                            onCellClicked = { r, c -> viewModel.onCellClicked(r, c) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                    }

                    // Right pane: Word Preview + Letter Wheel + Power-up Tools
                    Column(
                        modifier = Modifier
                            .weight(0.9f)
                            .fillMaxHeight()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        WordPreviewBar(
                            currentWord = uiState.currentWordInput,
                            feedbackState = uiState.feedbackState,
                            feedbackMessage = uiState.feedbackMessage,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        WheelAndControlsArea(
                            uiState = uiState,
                            viewModel = viewModel
                        )
                    }
                }
            } else {
                // Mobile Portrait Column Layout
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GameTopHeader(
                        uiState = uiState,
                        onPauseClicked = { viewModel.setPaused(true) },
                        onCoinsClicked = { viewModel.setStoreDialogVisible(true) },
                        onDictionaryClicked = { onNavigateToDictionary(null) }
                    )

                    // Target hint banner
                    if (uiState.isTargetHintModeActive) {
                        Surface(
                            color = AmberGold,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        ) {
                            Text(
                                text = "✨ Tap any hidden cell to reveal letter! (30 Coins)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Crossword Grid area
                    Box(
                        modifier = Modifier
                            .weight(1.2f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CrosswordGrid(
                            gridCells = uiState.gridCells,
                            recentlyFoundCoords = uiState.recentlyFoundCoords,
                            onCellClicked = { r, c -> viewModel.onCellClicked(r, c) },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Word Preview Bar (above wheel)
                    WordPreviewBar(
                        currentWord = uiState.currentWordInput,
                        feedbackState = uiState.feedbackState,
                        feedbackMessage = uiState.feedbackMessage,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    // Letter Wheel and controls area
                    Box(
                        modifier = Modifier
                            .weight(1.1f)
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        WheelAndControlsArea(
                            uiState = uiState,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }

        // Confetti Particle Celebration Overlay
        ConfettiOverlay(isActive = uiState.showConfetti)

        // Dialogs
        if (uiState.isPaused) {
            PauseDialog(
                onResume = { viewModel.setPaused(false) },
                onRestart = {
                    viewModel.setPaused(false)
                    viewModel.loadLevel(uiState.currentLevel.id, uiState.isDailyChallenge)
                },
                onLevelSelect = {
                    viewModel.setPaused(false)
                    onNavigateToLevels()
                },
                onHome = {
                    viewModel.setPaused(false)
                    onNavigateBack()
                },
                soundEnabled = uiState.soundEnabled,
                onToggleSound = { viewModel.toggleSound(it) },
                hapticsEnabled = uiState.hapticsEnabled,
                onToggleHaptics = { viewModel.toggleHaptics(it) },
                syncState = syncState
            )
        }

        if (uiState.isLevelCompleted) {
            LevelCompleteDialog(
                level = uiState.currentLevel,
                starsEarned = uiState.starsEarnedThisLevel,
                coinsEarned = uiState.coinsEarnedThisLevel,
                discoveredWords = uiState.foundWords.toList(),
                onNextLevel = {
                    val nextId = uiState.currentLevel.id + 1
                    if (nextId <= LevelRepository.levels.size) {
                        viewModel.loadLevel(nextId)
                    } else {
                        onNavigateToLevels()
                    }
                },
                onReplay = {
                    viewModel.loadLevel(uiState.currentLevel.id)
                },
                onOpenDictionaryForWord = { word ->
                    onNavigateToDictionary(word)
                }
            )
        }

        if (uiState.showBonusWordsDialog) {
            BonusWordsDialog(
                bonusWordsFound = uiState.foundBonusWords,
                allBonusWords = uiState.currentLevel.bonusWords,
                onDismiss = { viewModel.setBonusWordsDialogVisible(false) }
            )
        }

        if (uiState.showStoreDialog) {
            StoreDialog(
                coins = uiState.coins,
                isVip = uiState.isVip,
                onBuyCoins = { viewModel.buyCoins(it) },
                onActivateVip = { viewModel.activateVip() },
                onWatchAdForCoins = {
                    viewModel.setStoreDialogVisible(false)
                    viewModel.setAdDialogVisible(true, "50 Coins")
                },
                onDismiss = { viewModel.setStoreDialogVisible(false) }
            )
        }

        if (uiState.showAdSimulationDialog) {
            AdSimulationDialog(
                rewardRewardType = uiState.adRewardType,
                onRewardEarned = { viewModel.grantAdReward() },
                onDismiss = { viewModel.setAdDialogVisible(false) }
            )
        }

        if (uiState.showTutorial) {
            TutorialOverlay(
                onDismiss = { viewModel.dismissTutorial() }
            )
        }
    }
}

@Composable
private fun GameTopHeader(
    uiState: GameUiState,
    onPauseClicked: () -> Unit,
    onCoinsClicked: () -> Unit,
    onDictionaryClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onPauseClicked,
            modifier = Modifier.testTag("pause_icon_button")
        ) {
            Icon(Icons.Default.Pause, contentDescription = "Pause", tint = MaterialTheme.colorScheme.onSurface)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (uiState.isDailyChallenge) "DAILY CHALLENGE" else "LEVEL ${uiState.currentLevel.id}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${uiState.currentLevel.category} • ${uiState.currentLevel.title}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Dictionary quick button
            IconButton(
                onClick = onDictionaryClicked,
                modifier = Modifier.testTag("dictionary_quick_button")
            ) {
                Icon(Icons.Default.Book, contentDescription = "Dictionary", tint = TealPrimary)
            }

            // Coin pill
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = AmberGold.copy(alpha = 0.15f),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onCoinsClicked() }
                    .testTag("coin_pill")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = AmberGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${uiState.coins}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun WheelAndControlsArea(
    uiState: GameUiState,
    viewModel: GameViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // Left Side: Bonus Words Vault & Chest
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BadgedBox(
                badge = {
                    if (uiState.foundBonusWords.isNotEmpty()) {
                        Badge(
                            containerColor = AmberGold,
                            contentColor = Color.Black
                        ) {
                            Text("${uiState.foundBonusWords.size}", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shadowElevation = 3.dp,
                    modifier = Modifier
                        .size(52.dp)
                        .testTag("bonus_words_button")
                ) {
                    IconButton(onClick = { viewModel.setBonusWordsDialogVisible(true) }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Bonus Words Vault",
                            tint = AmberGold,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
            Text("Bonus", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        // Center: Letter Wheel Canvas
        LetterWheelCanvas(
            letters = uiState.wheelLetters,
            onWordChanged = { viewModel.onWordInputChanged(it) },
            onWordCompleted = { viewModel.onWordSubmitted(it) },
            onLetterAdded = { viewModel.onLetterAdded(it) },
            onShuffleClicked = { viewModel.shuffleWheel() },
            shuffleTrigger = uiState.shuffleTrigger,
            modifier = Modifier.size(240.dp)
        )

        // Right Side: Power-ups (Hint bulb & Magic wand)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Hint Bulb
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 3.dp,
                modifier = Modifier
                    .size(52.dp)
                    .testTag("hint_bulb_button")
            ) {
                IconButton(onClick = { viewModel.useLetterHint() }) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Hint (25 Coins)",
                        tint = AmberGold,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
            Text("Hint", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            // Magic Wand target hint
            Surface(
                shape = CircleShape,
                color = if (uiState.isTargetHintModeActive) AmberGold else MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 3.dp,
                modifier = Modifier
                    .size(52.dp)
                    .testTag("magic_wand_button")
            ) {
                IconButton(onClick = { viewModel.toggleTargetHintMode() }) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Target Hint",
                        tint = if (uiState.isTargetHintModeActive) Color.Black else TealPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Text("Target", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
