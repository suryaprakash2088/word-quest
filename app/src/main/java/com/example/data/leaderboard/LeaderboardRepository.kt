package com.example.data.leaderboard

import com.example.data.model.LeaderboardEntry

object LeaderboardRepository {

    private val baseRivals = listOf(
        LeaderboardEntry(1, "Aria_Lexicon", 4820, 48, 0xFF6366F1, "Master Tier"),
        LeaderboardEntry(2, "WordWizard_Kai", 4650, 46, 0xFFEC4899, "Master Tier"),
        LeaderboardEntry(3, "ElenaSpellbound", 4320, 43, 0xFF10B981, "Diamond Tier"),
        LeaderboardEntry(4, "PuzzlePro_Max", 3990, 39, 0xFFF59E0B, "Diamond Tier"),
        LeaderboardEntry(5, "Luna_Anagrams", 3750, 37, 0xFF8B5CF6, "Platinum Tier"),
        LeaderboardEntry(6, "CipherSam", 3480, 34, 0xFF06B6D4, "Platinum Tier"),
        LeaderboardEntry(7, "RowanRiddles", 3100, 31, 0xFF14B8A6, "Gold Tier"),
        LeaderboardEntry(8, "VesperWordsmith", 2850, 28, 0xFFF97316, "Gold Tier"),
        LeaderboardEntry(9, "EchoEnigma", 2600, 26, 0xFF84CC16, "Silver Tier"),
        LeaderboardEntry(10, "SolomonCross", 2350, 23, 0xFFA855F7, "Silver Tier")
    )

    fun getWeeklyLeaderboard(userScore: Int, userStars: Int, userName: String): List<LeaderboardEntry> {
        val userTier = when {
            userScore >= 4500 -> "Master Tier"
            userScore >= 3800 -> "Diamond Tier"
            userScore >= 3200 -> "Platinum Tier"
            userScore >= 2500 -> "Gold Tier"
            userScore >= 1500 -> "Silver Tier"
            else -> "Bronze Tier"
        }

        val userEntry = LeaderboardEntry(
            rank = 0,
            name = userName,
            score = userScore,
            stars = userStars,
            avatarColorHex = 0xFFF59E0B,
            tier = userTier,
            isCurrentUser = true
        )

        val combined = (baseRivals + userEntry).sortedByDescending { it.score }
        return combined.mapIndexed { index, entry ->
            entry.copy(rank = index + 1)
        }
    }
}
