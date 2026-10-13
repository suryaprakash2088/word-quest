# Word Quest — Wordscapes-Style Word Puzzle Game for Android

Word Quest is a production-ready, highly polished Wordscapes-style word puzzle game built with modern Android standards: Kotlin, Jetpack Compose, MVVM Architecture, and Room local persistence.

---

## 1. Architectural Choices

The app follows **Clean Architecture & MVVM (Model-View-ViewModel)** principles:
- **Presentation Layer (Jetpack Compose)**: Fully declarative, reactive UI using Material 3 design tokens. Screens observe UI state via `StateFlow` from `GameViewModel`. UI logic is decoupled into modular components (`CrosswordGrid`, `LetterWheelCanvas`, `WordPreviewBar`, `ConfettiOverlay`).
- **ViewModel Layer (`GameViewModel`)**: Manages the single source of truth (`GameUiState`), orchestrates word validation, coordinates power-ups (shuffles, single-letter hints, target wand hints), handles monetization rewards, and triggers sound & haptics.
- **Data Layer (`Room Database` & `Repositories`)**:
  - `LevelRepository`: Contains 16 handcrafted, mathematically verified crossword levels with intersecting coordinates, plus dynamic daily challenge puzzle generation.
  - `DictionaryRepository`: Built-in dictionary containing word definitions, parts of speech, and contextual example sentences.
  - `AppDatabase` (Room): Persists level progress, stars, discovered word history, and player profiles offline.
  - `CloudSyncManager`: Provides multi-device cloud synchronization with offline-first caching and real-time status indication.
  - `LeaderboardRepository`: Simulates competitive global and weekly tournament leagues with score tiers (Bronze to Master).

---

## 2. Custom Swipe / Letter Wheel Gesture Logic

The circular letter wheel uses **custom Jetpack Compose Canvas rendering combined with low-level pointer tracking** (`pointerInput` and `awaitEachGesture`):
1. **Node Geometry Computation**: Letter positions are calculated equidistant along the circular track using trigonometric polar coordinates ($x = c_x + r \cos(\theta), y = c_y + r \sin(\theta)$) taking into account animated shuffle rotation angles.
2. **Hit Detection & Debouncing**:
   - On initial down-press (`awaitFirstDown`), the system calculates Euclidean distance $\sqrt{(x - x_0)^2 + (y - y_0)^2}$ to all letter nodes. If within $1.3 \times \text{radius}$, the letter is activated.
   - During continuous drag, movements are tracked frame-by-frame. When the finger enters an unselected letter node's activation radius ($1.15 \times \text{radius}$), it snaps and triggers an acoustic rising scale tone and haptic feedback.
   - Letters cannot be duplicated in the current gesture path unless multiple instances of the letter exist on the wheel.
3. **Dynamic Connecting Line**: A 2-layer vector path is rendered in real time: a wide glowing backdrop line ($16\text{dp}$ width with alpha) and a core vibrant amber stroke ($7\text{dp}$ width). The line connects all selected letter nodes in sequence and dynamically rubber-bands to the active finger coordinate until released.
4. **Gesture Completion**: On finger lift, the constructed word is verified immediately against the grid and bonus dictionary, followed by clean state reset and victory/error animations.

---

## 3. Level Data Structure & Crossword Geometry

Level data is modeled cleanly using structured data classes (`Level`, `PlacedWord`, `GridCell`):
```kotlin
data class PlacedWord(
    val id: String,
    val word: String,
    val startRow: Int,
    val startCol: Int,
    val isVertical: Boolean,
    val clue: String = ""
)
```
- Each crossword level is represented on a 2D matrix where horizontal and vertical words share common intersection cells with matching characters.
- Unused matrix cells remain blank, while active cells render rounded wooden-style tiles with depth shadows.
- When words are discovered, letter tiles perform a spring bounce animation and reveal their character.
- Extra valid dictionary words outside the main crossword grid are captured as **Bonus Words**, awarding bonus coins and populating the player's personal vocabulary bank.

---

## 4. State Retention & Navigation Hygiene

- **Android Lifecycle & Rotation**: Game state is retained inside Android `ViewModel` and backed by Room SQLite, ensuring zero progress loss across orientation changes, multi-window splits, or background app switching.
- **Navigation Hygiene**: Powered by `androidx.navigation.compose` with strict backstack hygiene (`Home` $\leftrightarrow$ `LevelSelect` $\leftrightarrow$ `Gameplay` $\leftrightarrow$ `PauseDialog`).
- **System Back-Button**: Handled gracefully using Compose `BackHandler`, opening the Pause menu rather than abruptly killing the active game session.

---

## 5. Visual Polish, Audio & Micro-interactions

- **Synthesized Audio Engine (`GameSoundManager`)**: Real-time sound effects generated directly via Android `AudioTrack` (pentatonic rising connect tones, success chimes, bonus sparkles, buzz errors, and level completion fanfares) with zero audio file lag.
- **Haptic Feedback**: Integrates system vibrator effects for light ticks on letter selection, medium clicks on bonus words, and success patterns on level clears.
- **Celebration Particle System (`ConfettiOverlay`)**: 60+ physics-driven celebration confetti particles on level completion.
- **Adaptive Screen Layouts**: Responsive support for phones and tablets/foldables using `BoxWithConstraints` (vertical stack on mobile; side-by-side split screen on wide displays).
- **Monetization & Engagement**:
  - Sponsored video ads for free hints and coins.
  - VIP Explorer Pass (removes ads, 2x coin multiplier, golden word tiles).
  - Daily Challenge with calendar streaks and push notification reminders.
  - Educational Dictionary lookup tool with search and pronunciation.
  - Global Weekly Tournament Leaderboards with tier progression.
