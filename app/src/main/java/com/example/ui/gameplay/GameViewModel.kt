package com.example.ui.gameplay

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.GameSoundManager
import com.example.data.dictionary.DictionaryRepository
import com.example.data.levels.LevelRepository
import com.example.data.local.AppDatabase
import com.example.data.local.entity.DiscoveredWordEntity
import com.example.data.local.entity.LevelProgressEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.GridCell
import com.example.data.model.Level
import com.example.data.model.PlacedWord
import com.example.data.sync.CloudSyncManager
import com.example.data.sync.SyncState
import com.example.ui.components.WordFeedbackState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GameUiState(
    val currentLevel: Level,
    val gridCells: List<List<GridCell>> = emptyList(),
    val wheelLetters: List<Char> = emptyList(),
    val foundWords: Set<String> = emptySet(),
    val foundBonusWords: Set<String> = emptySet(),
    val revealedHintCoords: Set<String> = emptySet(),
    val currentWordInput: String = "",
    val feedbackState: WordFeedbackState = WordFeedbackState.IDLE,
    val feedbackMessage: String = "",
    val recentlyFoundCoords: Set<String> = emptySet(),
    val coins: Int = 200,
    val totalStars: Int = 0,
    val isLevelCompleted: Boolean = false,
    val starsEarnedThisLevel: Int = 0,
    val coinsEarnedThisLevel: Int = 0,
    val isPaused: Boolean = false,
    val isVip: Boolean = false,
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val shuffleTrigger: Int = 0,
    val showConfetti: Boolean = false,
    val isTargetHintModeActive: Boolean = false,
    val showTutorial: Boolean = false,
    val showBonusWordsDialog: Boolean = false,
    val showStoreDialog: Boolean = false,
    val showAdSimulationDialog: Boolean = false,
    val adRewardType: String = "50 Coins",
    val isDailyChallenge: Boolean = false
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val gameDao = db.gameDao()
    val soundManager = GameSoundManager(application)
    val syncManager = CloudSyncManager(application, db)

    private val _uiState = MutableStateFlow(
        GameUiState(
            currentLevel = LevelRepository.levels.first(),
            wheelLetters = LevelRepository.levels.first().wheelLetters
        )
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    val syncState: StateFlow<SyncState> = syncManager.syncState

    init {
        loadUserProfile()
        loadLevel(1)
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val user = gameDao.getUserProfileOnce() ?: UserProfileEntity()
            gameDao.saveUserProfile(user)
            soundManager.isSoundEnabled = user.soundEnabled
            soundManager.isHapticsEnabled = user.hapticsEnabled
            _uiState.value = _uiState.value.copy(
                coins = user.coins,
                totalStars = user.totalStars,
                isVip = user.isVip,
                soundEnabled = user.soundEnabled,
                hapticsEnabled = user.hapticsEnabled
            )
        }
    }

    fun loadLevel(levelId: Int, isDaily: Boolean = false) {
        val level = if (isDaily) {
            LevelRepository.getDailyChallengeLevel()
        } else {
            LevelRepository.getLevelById(levelId)
        }

        viewModelScope.launch {
            val savedProgress = if (!isDaily) gameDao.getLevelProgress(level.id) else null
            val foundWords = savedProgress?.foundWordsJoined
                ?.split(",")
                ?.filter { it.isNotEmpty() }
                ?.toSet() ?: emptySet()
            val foundBonusWords = savedProgress?.foundBonusWordsJoined
                ?.split(",")
                ?.filter { it.isNotEmpty() }
                ?.toSet() ?: emptySet()
            val revealedHints = savedProgress?.revealedHintsJoined
                ?.split(";")
                ?.filter { it.isNotEmpty() }
                ?.toSet() ?: emptySet()

            val grid = buildGridForLevel(level, foundWords, revealedHints)

            _uiState.value = _uiState.value.copy(
                currentLevel = level,
                gridCells = grid,
                wheelLetters = level.wheelLetters.shuffled(),
                foundWords = foundWords,
                foundBonusWords = foundBonusWords,
                revealedHintCoords = revealedHints,
                currentWordInput = "",
                feedbackState = WordFeedbackState.IDLE,
                feedbackMessage = "",
                recentlyFoundCoords = emptySet(),
                isLevelCompleted = false,
                starsEarnedThisLevel = 0,
                coinsEarnedThisLevel = 0,
                showConfetti = false,
                isDailyChallenge = isDaily,
                showTutorial = (levelId == 1 && !isDaily && foundWords.isEmpty())
            )
        }
    }

    private fun buildGridForLevel(
        level: Level,
        foundWords: Set<String>,
        revealedHints: Set<String>
    ): List<List<GridCell>> {
        val rows = level.rows
        val cols = level.cols
        val mutableGrid = MutableList(rows) { r ->
            MutableList(cols) { c ->
                GridCell(row = r, col = c)
            }
        }

        level.placedWords.forEach { placedWord ->
            val word = placedWord.word
            val isFound = foundWords.contains(word)

            for (i in word.indices) {
                val r = if (placedWord.isVertical) placedWord.startRow + i else placedWord.startRow
                val c = if (placedWord.isVertical) placedWord.startCol else placedWord.startCol + i

                if (r in 0 until rows && c in 0 until cols) {
                    val existing = mutableGrid[r][c]
                    val isHinted = revealedHints.contains("$r,$c")
                    val isRevealed = isFound || existing.isRevealed

                    mutableGrid[r][c] = existing.copy(
                        letter = word[i],
                        isPartOfWord = true,
                        isRevealed = isRevealed,
                        isHintRevealed = isHinted,
                        wordIds = existing.wordIds + placedWord.id
                    )
                }
            }
        }

        return mutableGrid
    }

    fun onWordInputChanged(input: String) {
        _uiState.value = _uiState.value.copy(
            currentWordInput = input,
            feedbackState = if (input.isNotEmpty()) WordFeedbackState.SWIPING else WordFeedbackState.IDLE
        )
    }

    fun onLetterAdded(indexInPath: Int) {
        soundManager.playLetterSelect(indexInPath)
    }

    fun onWordSubmitted(word: String) {
        if (word.length < 2) {
            _uiState.value = _uiState.value.copy(
                currentWordInput = "",
                feedbackState = WordFeedbackState.IDLE
            )
            return
        }

        val level = _uiState.value.currentLevel
        val placedWordMatch = level.placedWords.firstOrNull { it.word.equals(word, ignoreCase = true) }

        if (placedWordMatch != null) {
            val upperWord = placedWordMatch.word
            if (_uiState.value.foundWords.contains(upperWord)) {
                // Already discovered word
                soundManager.triggerHaptic(GameSoundManager.HapticType.LIGHT_TICK)
                showTemporaryFeedback(WordFeedbackState.ALREADY_FOUND, "Already found!")
            } else {
                // Brand new puzzle word!
                handleValidPlacedWord(placedWordMatch)
            }
            return
        }

        // Check if bonus word
        val isBonus = level.bonusWords.any { it.equals(word, ignoreCase = true) } ||
                canFormWordFromLetters(word, level.wheelLetters)

        if (isBonus) {
            val upperWord = word.uppercase()
            if (_uiState.value.foundBonusWords.contains(upperWord)) {
                showTemporaryFeedback(WordFeedbackState.ALREADY_FOUND, "Already found!")
            } else {
                handleValidBonusWord(upperWord)
            }
            return
        }

        // Invalid word
        soundManager.playInvalidWord()
        showTemporaryFeedback(WordFeedbackState.INVALID, "Not in word list")
    }

    private fun canFormWordFromLetters(word: String, wheelLetters: List<Char>): Boolean {
        if (word.length < 3) return false
        val available = wheelLetters.map { it.uppercaseChar() }.toMutableList()
        for (ch in word.uppercase()) {
            if (!available.remove(ch)) return false
        }
        val entry = DictionaryRepository.lookup(word)
        return entry.definition.isNotEmpty()
    }

    private fun handleValidPlacedWord(placedWord: PlacedWord) {
        val word = placedWord.word
        val updatedFoundWords = _uiState.value.foundWords + word
        soundManager.playWordSuccess()

        // Highlight cells for animation
        val recentlyFound = mutableSetOf<String>()
        for (i in word.indices) {
            val r = if (placedWord.isVertical) placedWord.startRow + i else placedWord.startRow
            val c = if (placedWord.isVertical) placedWord.startCol else placedWord.startCol + i
            recentlyFound.add("$r,$c")
        }

        val baseCoins = if (_uiState.value.isVip) 20 else 10
        val updatedCoins = _uiState.value.coins + baseCoins

        val updatedGrid = buildGridForLevel(
            _uiState.value.currentLevel,
            updatedFoundWords,
            _uiState.value.revealedHintCoords
        )

        val isCompleted = _uiState.value.currentLevel.placedWords.all { updatedFoundWords.contains(it.word) }

        _uiState.value = _uiState.value.copy(
            foundWords = updatedFoundWords,
            gridCells = updatedGrid,
            coins = updatedCoins,
            recentlyFoundCoords = recentlyFound,
            feedbackState = WordFeedbackState.SUCCESS,
            feedbackMessage = "+$baseCoins Coins!"
        )

        // Record in room database
        viewModelScope.launch {
            gameDao.insertDiscoveredWord(
                DiscoveredWordEntity(
                    word = word,
                    levelSource = _uiState.value.currentLevel.id,
                    isBonus = false
                )
            )
            saveCurrentProgress(isCompleted)
        }

        if (isCompleted) {
            triggerLevelCompletion()
        } else {
            viewModelScope.launch {
                delay(1200)
                _uiState.value = _uiState.value.copy(
                    recentlyFoundCoords = emptySet(),
                    feedbackState = WordFeedbackState.IDLE,
                    feedbackMessage = "",
                    currentWordInput = ""
                )
            }
        }
    }

    private fun handleValidBonusWord(word: String) {
        soundManager.playBonusWord()
        val updatedBonus = _uiState.value.foundBonusWords + word
        val bonusCoins = if (_uiState.value.isVip) 10 else 5
        val updatedCoins = _uiState.value.coins + bonusCoins

        _uiState.value = _uiState.value.copy(
            foundBonusWords = updatedBonus,
            coins = updatedCoins,
            feedbackState = WordFeedbackState.BONUS,
            feedbackMessage = "★ Bonus! +$bonusCoins Coins"
        )

        viewModelScope.launch {
            gameDao.insertDiscoveredWord(
                DiscoveredWordEntity(
                    word = word,
                    levelSource = _uiState.value.currentLevel.id,
                    isBonus = true
                )
            )
            saveCurrentProgress(false)
            delay(1300)
            _uiState.value = _uiState.value.copy(
                feedbackState = WordFeedbackState.IDLE,
                feedbackMessage = "",
                currentWordInput = ""
            )
        }
    }

    private fun triggerLevelCompletion() {
        soundManager.playLevelComplete()
        val earnedStars = 3
        val levelClearBonus = if (_uiState.value.isVip) 60 else 30
        val finalCoins = _uiState.value.coins + levelClearBonus
        val finalStars = _uiState.value.totalStars + earnedStars

        _uiState.value = _uiState.value.copy(
            isLevelCompleted = true,
            starsEarnedThisLevel = earnedStars,
            coinsEarnedThisLevel = levelClearBonus,
            coins = finalCoins,
            totalStars = finalStars,
            showConfetti = true
        )

        viewModelScope.launch {
            val user = gameDao.getUserProfileOnce() ?: UserProfileEntity()
            gameDao.saveUserProfile(
                user.copy(
                    coins = finalCoins,
                    totalStars = finalStars,
                    currentLevelId = (_uiState.value.currentLevel.id + 1).coerceAtMost(LevelRepository.levels.size)
                )
            )
            syncManager.performSync()
        }
    }

    private fun showTemporaryFeedback(state: WordFeedbackState, msg: String) {
        _uiState.value = _uiState.value.copy(
            feedbackState = state,
            feedbackMessage = msg
        )
        viewModelScope.launch {
            delay(1100)
            _uiState.value = _uiState.value.copy(
                feedbackState = WordFeedbackState.IDLE,
                feedbackMessage = "",
                currentWordInput = ""
            )
        }
    }

    private suspend fun saveCurrentProgress(isCompleted: Boolean) {
        val level = _uiState.value.currentLevel
        val entity = LevelProgressEntity(
            levelId = level.id,
            isCompleted = isCompleted,
            stars = if (isCompleted) 3 else 0,
            foundWordsJoined = _uiState.value.foundWords.joinToString(","),
            foundBonusWordsJoined = _uiState.value.foundBonusWords.joinToString(","),
            revealedHintsJoined = _uiState.value.revealedHintCoords.joinToString(";"),
            timeSpentSeconds = 60
        )
        gameDao.saveLevelProgress(entity)
    }

    // Power-up: Shuffle letters
    fun shuffleWheel() {
        soundManager.playShuffle()
        _uiState.value = _uiState.value.copy(
            wheelLetters = _uiState.value.wheelLetters.shuffled(),
            shuffleTrigger = _uiState.value.shuffleTrigger + 1
        )
    }

    // Power-up: Letter Hint
    fun useLetterHint() {
        val hintCost = 25
        if (_uiState.value.coins < hintCost) {
            // Offer ad or store
            _uiState.value = _uiState.value.copy(
                showAdSimulationDialog = true,
                adRewardType = "Free Letter Hint"
            )
            return
        }

        // Find an unrevealed grid cell
        val unrevealedCells = mutableListOf<Pair<Int, Int>>()
        _uiState.value.gridCells.forEachIndexed { r, row ->
            row.forEachIndexed { c, cell ->
                if (cell.isPartOfWord && !cell.isRevealed && !cell.isHintRevealed) {
                    unrevealedCells.add(r to c)
                }
            }
        }

        if (unrevealedCells.isNotEmpty()) {
            soundManager.playHint()
            val pick = unrevealedCells.random()
            val newCoord = "${pick.first},${pick.second}"
            val updatedHints = _uiState.value.revealedHintCoords + newCoord
            val updatedCoins = _uiState.value.coins - hintCost

            val updatedGrid = buildGridForLevel(
                _uiState.value.currentLevel,
                _uiState.value.foundWords,
                updatedHints
            )

            _uiState.value = _uiState.value.copy(
                revealedHintCoords = updatedHints,
                gridCells = updatedGrid,
                coins = updatedCoins
            )

            viewModelScope.launch {
                val user = gameDao.getUserProfileOnce() ?: UserProfileEntity()
                gameDao.saveUserProfile(user.copy(coins = updatedCoins))
            }
        }
    }

    // Power-up: Target Cell Hint (Magic Wand)
    fun toggleTargetHintMode() {
        _uiState.value = _uiState.value.copy(
            isTargetHintModeActive = !_uiState.value.isTargetHintModeActive
        )
    }

    fun onCellClicked(row: Int, col: Int) {
        if (!_uiState.value.isTargetHintModeActive) return

        val cell = _uiState.value.gridCells.getOrNull(row)?.getOrNull(col) ?: return
        if (cell.isPartOfWord && !cell.isRevealed && !cell.isHintRevealed) {
            val cost = 30
            if (_uiState.value.coins >= cost) {
                soundManager.playHint()
                val updatedHints = _uiState.value.revealedHintCoords + "$row,$col"
                val updatedCoins = _uiState.value.coins - cost
                val updatedGrid = buildGridForLevel(
                    _uiState.value.currentLevel,
                    _uiState.value.foundWords,
                    updatedHints
                )
                _uiState.value = _uiState.value.copy(
                    revealedHintCoords = updatedHints,
                    gridCells = updatedGrid,
                    coins = updatedCoins,
                    isTargetHintModeActive = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isTargetHintModeActive = false,
                    showAdSimulationDialog = true,
                    adRewardType = "Magic Wand Hint"
                )
            }
        }
    }

    // Monetization actions
    fun grantAdReward() {
        val bonus = 50
        val updatedCoins = _uiState.value.coins + bonus
        _uiState.value = _uiState.value.copy(
            coins = updatedCoins,
            showAdSimulationDialog = false
        )
        soundManager.playWordSuccess()
        viewModelScope.launch {
            val user = gameDao.getUserProfileOnce() ?: UserProfileEntity()
            gameDao.saveUserProfile(user.copy(coins = updatedCoins))
        }
    }

    fun activateVip() {
        _uiState.value = _uiState.value.copy(
            isVip = true,
            coins = _uiState.value.coins + 300,
            showStoreDialog = false
        )
        soundManager.playWordSuccess()
        viewModelScope.launch {
            val user = gameDao.getUserProfileOnce() ?: UserProfileEntity()
            gameDao.saveUserProfile(user.copy(isVip = true, coins = _uiState.value.coins))
        }
    }

    fun buyCoins(amount: Int) {
        val updated = _uiState.value.coins + amount
        _uiState.value = _uiState.value.copy(coins = updated)
        soundManager.playWordSuccess()
        viewModelScope.launch {
            val user = gameDao.getUserProfileOnce() ?: UserProfileEntity()
            gameDao.saveUserProfile(user.copy(coins = updated))
        }
    }

    fun toggleSound(enabled: Boolean) {
        soundManager.isSoundEnabled = enabled
        _uiState.value = _uiState.value.copy(soundEnabled = enabled)
        viewModelScope.launch {
            val user = gameDao.getUserProfileOnce() ?: UserProfileEntity()
            gameDao.saveUserProfile(user.copy(soundEnabled = enabled))
        }
    }

    fun toggleHaptics(enabled: Boolean) {
        soundManager.isHapticsEnabled = enabled
        _uiState.value = _uiState.value.copy(hapticsEnabled = enabled)
        viewModelScope.launch {
            val user = gameDao.getUserProfileOnce() ?: UserProfileEntity()
            gameDao.saveUserProfile(user.copy(hapticsEnabled = enabled))
        }
    }

    fun setPaused(paused: Boolean) {
        _uiState.value = _uiState.value.copy(isPaused = paused)
    }

    fun dismissTutorial() {
        _uiState.value = _uiState.value.copy(showTutorial = false)
    }

    fun setBonusWordsDialogVisible(visible: Boolean) {
        _uiState.value = _uiState.value.copy(showBonusWordsDialog = visible)
    }

    fun setStoreDialogVisible(visible: Boolean) {
        _uiState.value = _uiState.value.copy(showStoreDialog = visible)
    }

    fun setAdDialogVisible(visible: Boolean, rewardType: String = "50 Coins") {
        _uiState.value = _uiState.value.copy(
            showAdSimulationDialog = visible,
            adRewardType = rewardType
        )
    }
}
