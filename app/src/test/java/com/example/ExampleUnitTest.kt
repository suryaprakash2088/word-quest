package com.example

import com.example.data.dictionary.DictionaryRepository
import com.example.data.levels.LevelRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testLevelRepository_has16PlayableLevels() {
        val levels = LevelRepository.levels
        assertTrue("Should contain at least 16 levels", levels.size >= 16)

        levels.forEach { level ->
            assertTrue("Level ${level.id} should have wheel letters", level.wheelLetters.isNotEmpty())
            assertTrue("Level ${level.id} should have placed words", level.placedWords.isNotEmpty())

            // Verify each placed word can be formed from wheel letters
            level.placedWords.forEach { placedWord ->
                val availableLetters = level.wheelLetters.toMutableList()
                placedWord.word.forEach { ch ->
                    val removed = availableLetters.remove(ch)
                    assertTrue(
                        "Word '${placedWord.word}' in level ${level.id} contains letter '$ch' not in wheel ${level.wheelLetters}",
                        removed
                    )
                }
            }
        }
    }

    @Test
    fun testCrosswordIntersections_matchCharactersExactly() {
        LevelRepository.levels.forEach { level ->
            val grid = Array(level.rows) { Array<Char?>(level.cols) { null } }

            level.placedWords.forEach { placedWord ->
                for (i in placedWord.word.indices) {
                    val r = if (placedWord.isVertical) placedWord.startRow + i else placedWord.startRow
                    val c = if (placedWord.isVertical) placedWord.startCol else placedWord.startCol + i

                    assertTrue("Row $r should be within bounds 0..${level.rows}", r in 0 until level.rows)
                    assertTrue("Col $c should be within bounds 0..${level.cols}", c in 0 until level.cols)

                    val char = placedWord.word[i]
                    val existing = grid[r][c]
                    if (existing != null) {
                        assertEquals(
                            "Intersection mismatch at ($r, $c) in level ${level.id} for word ${placedWord.word}: expected '$existing' but found '$char'",
                            existing,
                            char
                        )
                    } else {
                        grid[r][c] = char
                    }
                }
            }
        }
    }

    @Test
    fun testDictionaryRepository_lookupWorks() {
        val entry = DictionaryRepository.lookup("SPAN")
        assertEquals("SPAN", entry.word)
        assertTrue(entry.definition.isNotEmpty())

        val searchResults = DictionaryRepository.searchWords("OCEAN")
        assertTrue(searchResults.any { it.word == "OCEAN" })
    }

    @Test
    fun testDailyChallenge_generatesValidLevel() {
        val daily = LevelRepository.getDailyChallengeLevel()
        assertNotNull(daily)
        assertEquals(999, daily.id)
        assertTrue(daily.placedWords.isNotEmpty())
    }
}
