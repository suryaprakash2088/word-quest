package com.example.data.levels

import com.example.data.model.Level
import com.example.data.model.PlacedWord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LevelRepository {

    val levels: List<Level> = listOf(
        // Level 1
        Level(
            id = 1,
            title = "Morning Dew",
            category = "Sunrise",
            wheelLetters = listOf('S', 'P', 'A', 'N'),
            placedWords = listOf(
                PlacedWord("w1_1", "SPAN", startRow = 1, startCol = 0, isVertical = false, clue = "The full extent of something"),
                PlacedWord("w1_2", "PAN", startRow = 1, startCol = 1, isVertical = true, clue = "Shallow metal container for cooking"),
                PlacedWord("w1_3", "NAP", startRow = 1, startCol = 3, isVertical = true, clue = "A short sleep during the day")
            ),
            bonusWords = setOf("SNAP", "ASP", "SPA", "SAN"),
            rows = 5,
            cols = 5
        ),

        // Level 2
        Level(
            id = 2,
            title = "Gentle Breeze",
            category = "Sunrise",
            wheelLetters = listOf('S', 'T', 'A', 'R'),
            placedWords = listOf(
                PlacedWord("w2_1", "STAR", startRow = 1, startCol = 0, isVertical = false, clue = "A luminous point in the night sky"),
                PlacedWord("w2_2", "ART", startRow = 1, startCol = 2, isVertical = true, clue = "Expression of human creative skill"),
                PlacedWord("w2_3", "RAT", startRow = 1, startCol = 3, isVertical = true, clue = "A rodent larger than a mouse")
            ),
            bonusWords = setOf("TAR", "SAT", "RATS", "TSAR"),
            rows = 5,
            cols = 5
        ),

        // Level 3
        Level(
            id = 3,
            title = "Meadow Bloom",
            category = "Bloom",
            wheelLetters = listOf('L', 'E', 'A', 'F'),
            placedWords = listOf(
                PlacedWord("w3_1", "LEAF", startRow = 1, startCol = 0, isVertical = false, clue = "Flattened green organ of a plant"),
                PlacedWord("w3_2", "FLEA", startRow = 1, startCol = 3, isVertical = true, clue = "Small wingless jumping insect"),
                PlacedWord("w3_3", "ALE", startRow = 3, startCol = 1, isVertical = false, clue = "A type of beer")
            ),
            bonusWords = setOf("ELF"),
            rows = 5,
            cols = 5
        ),

        // Level 4
        Level(
            id = 4,
            title = "Forest Canopy",
            category = "Bloom",
            wheelLetters = listOf('T', 'R', 'E', 'E', 'S'),
            placedWords = listOf(
                PlacedWord("w4_1", "TREES", startRow = 1, startCol = 0, isVertical = false, clue = "Woody perennial plants"),
                PlacedWord("w4_2", "REST", startRow = 1, startCol = 1, isVertical = true, clue = "Cease work or movement to relax"),
                PlacedWord("w4_3", "SEE", startRow = 1, startCol = 4, isVertical = true, clue = "Perceive with the eyes")
            ),
            bonusWords = setOf("STEER", "RESET", "TEE", "SET"),
            rows = 6,
            cols = 6
        ),

        // Level 5
        Level(
            id = 5,
            title = "River Bank",
            category = "Water",
            wheelLetters = listOf('W', 'A', 'T', 'E', 'R'),
            placedWords = listOf(
                PlacedWord("w5_1", "WATER", startRow = 1, startCol = 0, isVertical = false, clue = "Essential clear liquid"),
                PlacedWord("w5_2", "WEAR", startRow = 1, startCol = 0, isVertical = true, clue = "Have clothing on body"),
                PlacedWord("w5_3", "TEAR", startRow = 1, startCol = 2, isVertical = true, clue = "Drop of clear liquid from eye"),
                PlacedWord("w5_4", "RAW", startRow = 1, startCol = 4, isVertical = true, clue = "Not cooked")
            ),
            bonusWords = setOf("RATE", "WART", "ERA", "WET", "EAR", "ATE"),
            rows = 6,
            cols = 6
        ),

        // Level 6
        Level(
            id = 6,
            title = "Ocean Wave",
            category = "Water",
            wheelLetters = listOf('O', 'C', 'E', 'A', 'N'),
            placedWords = listOf(
                PlacedWord("w6_1", "OCEAN", startRow = 1, startCol = 0, isVertical = false, clue = "Vast body of salt water"),
                PlacedWord("w6_2", "ONE", startRow = 1, startCol = 0, isVertical = true, clue = "Lowest cardinal number"),
                PlacedWord("w6_3", "CANOE", startRow = 1, startCol = 1, isVertical = true, clue = "Narrow lightweight boat"),
                PlacedWord("w6_4", "ACNE", startRow = 2, startCol = 1, isVertical = false, clue = "Skin condition")
            ),
            bonusWords = setOf("CONE", "ONCE", "CANE", "EON", "ACE"),
            rows = 6,
            cols = 6
        ),

        // Level 7
        Level(
            id = 7,
            title = "Golden Peak",
            category = "Canyon",
            wheelLetters = listOf('P', 'E', 'A', 'K', 'S'),
            placedWords = listOf(
                PlacedWord("w7_1", "PEAKS", startRow = 1, startCol = 0, isVertical = false, clue = "Pointed tops of mountains"),
                PlacedWord("w7_2", "APE", startRow = 1, startCol = 2, isVertical = true, clue = "Large tailless primate"),
                PlacedWord("w7_3", "SPEAK", startRow = 1, startCol = 4, isVertical = true, clue = "Convey information through speech")
            ),
            bonusWords = setOf("PEAK", "SEAP", "SPA", "SAP", "ASK"),
            rows = 6,
            cols = 6
        ),

        // Level 8
        Level(
            id = 8,
            title = "Sunset Glow",
            category = "Canyon",
            wheelLetters = listOf('S', 'U', 'N', 'L', 'I', 'T'),
            placedWords = listOf(
                PlacedWord("w8_1", "SUNLIT", startRow = 1, startCol = 0, isVertical = false, clue = "Illuminated by direct sunlight"),
                PlacedWord("w8_2", "SUIT", startRow = 1, startCol = 0, isVertical = true, clue = "Set of matching clothes"),
                PlacedWord("w8_3", "NUT", startRow = 1, startCol = 2, isVertical = true, clue = "Hard-shelled seed"),
                PlacedWord("w8_4", "TIN", startRow = 4, startCol = 0, isVertical = false, clue = "Silvery-white metal")
            ),
            bonusWords = setOf("UNIT", "SILT", "SLIT", "LIT", "SUN", "LIST", "SIN"),
            rows = 6,
            cols = 6
        ),

        // Level 9
        Level(
            id = 9,
            title = "Starlit Grove",
            category = "Cosmos",
            wheelLetters = listOf('P', 'L', 'A', 'N', 'E', 'T'),
            placedWords = listOf(
                PlacedWord("w9_1", "PLANET", startRow = 1, startCol = 0, isVertical = false, clue = "Celestial body moving in orbit"),
                PlacedWord("w9_2", "PLANT", startRow = 1, startCol = 0, isVertical = true, clue = "Living organism producing oxygen"),
                PlacedWord("w9_3", "LATE", startRow = 1, startCol = 1, isVertical = true, clue = "After expected time"),
                PlacedWord("w9_4", "NET", startRow = 1, startCol = 3, isVertical = true, clue = "Meshed fabric device")
            ),
            bonusWords = setOf("PLANE", "PANEL", "PLATE", "PLEAT", "TALE", "LEAP", "PALE", "TAP", "PET"),
            rows = 7,
            cols = 7
        ),

        // Level 10
        Level(
            id = 10,
            title = "Misty Vale",
            category = "Cosmos",
            wheelLetters = listOf('S', 'I', 'L', 'V', 'E', 'R'),
            placedWords = listOf(
                PlacedWord("w10_1", "SILVER", startRow = 1, startCol = 0, isVertical = false, clue = "Precious lustrous white metal"),
                PlacedWord("w10_2", "LIVE", startRow = 1, startCol = 2, isVertical = true, clue = "Remain alive"),
                PlacedWord("w10_3", "RISE", startRow = 1, startCol = 5, isVertical = true, clue = "Move upward")
            ),
            bonusWords = setOf("LIVER", "RIVER", "VEIL", "VILE", "SIRE", "SIR", "LIE"),
            rows = 7,
            cols = 7
        ),

        // Level 11
        Level(
            id = 11,
            title = "Zen Garden",
            category = "Harmony",
            wheelLetters = listOf('G', 'A', 'R', 'D', 'E', 'N'),
            placedWords = listOf(
                PlacedWord("w11_1", "GARDEN", startRow = 1, startCol = 0, isVertical = false, clue = "Plot of ground for growing flowers"),
                PlacedWord("w11_2", "READ", startRow = 1, startCol = 2, isVertical = true, clue = "Comprehend written words"),
                PlacedWord("w11_3", "DANGER", startRow = 1, startCol = 3, isVertical = true, clue = "Possibility of suffering harm")
            ),
            bonusWords = setOf("GRADE", "GRAND", "RANGE", "DEAR", "DARE", "GEAR", "RANG", "RAG", "RED"),
            rows = 7,
            cols = 7
        ),

        // Level 12
        Level(
            id = 12,
            title = "Ancient Temple",
            category = "Harmony",
            wheelLetters = listOf('C', 'A', 'S', 'T', 'L', 'E'),
            placedWords = listOf(
                PlacedWord("w12_1", "CASTLE", startRow = 1, startCol = 0, isVertical = false, clue = "Fortified medieval stronghold"),
                PlacedWord("w12_2", "SALE", startRow = 1, startCol = 2, isVertical = true, clue = "Exchange of goods for money"),
                PlacedWord("w12_3", "TALE", startRow = 1, startCol = 3, isVertical = true, clue = "A fictitious story")
            ),
            bonusWords = setOf("LEAST", "STEAL", "STALE", "SLATE", "LACE", "CASE", "SEAL", "CAT", "ACT"),
            rows = 7,
            cols = 7
        ),

        // Level 13
        Level(
            id = 13,
            title = "Desert Mirage",
            category = "Adventure",
            wheelLetters = listOf('B', 'R', 'I', 'D', 'G', 'E'),
            placedWords = listOf(
                PlacedWord("w13_1", "BRIDGE", startRow = 1, startCol = 0, isVertical = false, clue = "Structure carrying a road over obstacle"),
                PlacedWord("w13_2", "BIRD", startRow = 1, startCol = 0, isVertical = true, clue = "Warm-blooded feathered creature"),
                PlacedWord("w13_3", "RIDGE", startRow = 1, startCol = 1, isVertical = true, clue = "Long narrow hilltop range")
            ),
            bonusWords = setOf("GRID", "DIRE", "BRIDE", "RED", "BIG", "BED", "RIB"),
            rows = 7,
            cols = 7
        ),

        // Level 14
        Level(
            id = 14,
            title = "Alpine Crest",
            category = "Adventure",
            wheelLetters = listOf('F', 'L', 'I', 'G', 'H', 'T'),
            placedWords = listOf(
                PlacedWord("w14_1", "FLIGHT", startRow = 1, startCol = 0, isVertical = false, clue = "The process of flying"),
                PlacedWord("w14_2", "LIGHT", startRow = 1, startCol = 1, isVertical = true, clue = "Natural luminous agent"),
                PlacedWord("w14_3", "GIFT", startRow = 3, startCol = 1, isVertical = false, clue = "Something given voluntarily")
            ),
            bonusWords = setOf("FIGHT", "LIFT", "HIGH", "HIT", "FIT", "LIT", "FIG"),
            rows = 7,
            cols = 7
        ),

        // Level 15
        Level(
            id = 15,
            title = "Aurora Borealis",
            category = "Wonder",
            wheelLetters = listOf('W', 'O', 'N', 'D', 'E', 'R'),
            placedWords = listOf(
                PlacedWord("w15_1", "WONDER", startRow = 1, startCol = 0, isVertical = false, clue = "Feeling of surprise and admiration"),
                PlacedWord("w15_2", "WORD", startRow = 1, startCol = 0, isVertical = true, clue = "Single distinct element of speech"),
                PlacedWord("w15_3", "DOWN", startRow = 1, startCol = 3, isVertical = true, clue = "Toward a lower place")
            ),
            bonusWords = setOf("DROWN", "OWNED", "NODE", "RODE", "RED", "ROW", "ONE", "END"),
            rows = 7,
            cols = 7
        ),

        // Level 16
        Level(
            id = 16,
            title = "Infinity Gate",
            category = "Wonder",
            wheelLetters = listOf('S', 'P', 'R', 'I', 'N', 'G'),
            placedWords = listOf(
                PlacedWord("w16_1", "SPRING", startRow = 1, startCol = 0, isVertical = false, clue = "Season of renewal and blossoms"),
                PlacedWord("w16_2", "SING", startRow = 1, startCol = 0, isVertical = true, clue = "Make musical sounds with voice"),
                PlacedWord("w16_3", "RING", startRow = 1, startCol = 2, isVertical = true, clue = "Circular band worn on finger")
            ),
            bonusWords = setOf("GRIP", "SPIN", "PING", "PIG", "PIN", "RIP", "SIN", "SIP"),
            rows = 7,
            cols = 7
        )
    )

    fun getLevelById(id: Int): Level {
        return levels.firstOrNull { it.id == id } ?: levels.first()
    }

    fun getDailyChallengeLevel(date: Date = Date()): Level {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
        val seed = dateFormat.format(date).hashCode()
        val index = kotlin.math.abs(seed) % levels.size
        val baseLevel = levels[index]
        return baseLevel.copy(
            id = 999,
            title = "Daily Puzzle: ${SimpleDateFormat("MMM d", Locale.US).format(date)}",
            category = "Daily Star"
        )
    }
}
