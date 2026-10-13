package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "level_progress")
data class LevelProgressEntity(
    @PrimaryKey val levelId: Int,
    val isCompleted: Boolean,
    val stars: Int,
    val foundWordsJoined: String,
    val foundBonusWordsJoined: String,
    val revealedHintsJoined: String,
    val timeSpentSeconds: Long
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val playerName: String = "Word Explorer",
    val coins: Int = 200,
    val totalStars: Int = 0,
    val currentLevelId: Int = 1,
    val isVip: Boolean = false,
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val dailyStreak: Int = 1,
    val lastDailyDate: String = "",
    val offlineSyncStatus: String = "Synced",
    val lastSyncTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "discovered_words")
data class DiscoveredWordEntity(
    @PrimaryKey val word: String,
    val discoveredAtTimestamp: Long = System.currentTimeMillis(),
    val levelSource: Int,
    val isBonus: Boolean = false
)
