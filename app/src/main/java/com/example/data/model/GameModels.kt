package com.example.data.model

data class PlacedWord(
    val id: String,
    val word: String,
    val startRow: Int,
    val startCol: Int,
    val isVertical: Boolean,
    val clue: String = ""
)

data class GridCell(
    val row: Int,
    val col: Int,
    val letter: Char = ' ',
    val isPartOfWord: Boolean = false,
    val isRevealed: Boolean = false,
    val isHintRevealed: Boolean = false,
    val wordIds: List<String> = emptyList()
)

data class Level(
    val id: Int,
    val title: String,
    val category: String,
    val wheelLetters: List<Char>,
    val placedWords: List<PlacedWord>,
    val bonusWords: Set<String> = emptySet(),
    val rows: Int = 6,
    val cols: Int = 6
)

data class LevelProgress(
    val levelId: Int,
    val isCompleted: Boolean = false,
    val stars: Int = 0,
    val foundWords: Set<String> = emptySet(),
    val foundBonusWords: Set<String> = emptySet(),
    val revealedHints: Set<String> = emptySet(), // "r,c"
    val timeSpentSeconds: Long = 0L
)

data class DictionaryEntry(
    val word: String,
    val partOfSpeech: String,
    val definition: String,
    val example: String
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val score: Int,
    val stars: Int,
    val avatarColorHex: Long,
    val tier: String,
    val isCurrentUser: Boolean = false
)

data class DailyChallenge(
    val dateString: String,
    val level: Level,
    val completed: Boolean = false,
    val rewardCoins: Int = 100
)

data class PlayerStats(
    val totalWordsSolved: Int = 0,
    val bonusWordsFound: Int = 0,
    val hintsUsed: Int = 0,
    val totalPlayTimeMinutes: Int = 0,
    val dailyStreak: Int = 0,
    val accuracyPercentage: Int = 92
)
