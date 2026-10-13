package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RubyInvalid

enum class WordFeedbackState {
    IDLE,
    SWIPING,
    SUCCESS,
    BONUS,
    ALREADY_FOUND,
    INVALID
}

@Composable
fun WordPreviewBar(
    currentWord: String,
    feedbackState: WordFeedbackState,
    feedbackMessage: String,
    modifier: Modifier = Modifier
) {
    val isVisible = currentWord.isNotEmpty() || feedbackState != WordFeedbackState.IDLE

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = modifier.testTag("word_preview_bar")
    ) {
        val (bgColor, textColor, displayText) = when (feedbackState) {
            WordFeedbackState.SUCCESS -> Triple(EmeraldSuccess, Color.White, feedbackMessage.ifEmpty { currentWord })
            WordFeedbackState.BONUS -> Triple(AmberGold, Color.White, feedbackMessage.ifEmpty { "★ Bonus Word!" })
            WordFeedbackState.ALREADY_FOUND -> Triple(Color(0xFF64748B), Color.White, "Already found!")
            WordFeedbackState.INVALID -> Triple(RubyInvalid, Color.White, feedbackMessage.ifEmpty { "Not in puzzle" })
            WordFeedbackState.SWIPING, WordFeedbackState.IDLE -> Triple(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
                MaterialTheme.colorScheme.onSurfaceVariant,
                currentWord
            )
        }

        Box(
            modifier = Modifier
                .shadow(6.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(bgColor)
                .padding(horizontal = 24.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = textColor
            )
        }
    }
}
