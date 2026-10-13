package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.DiscoveredWordEntity
import com.example.data.local.entity.LevelProgressEntity
import com.example.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT * FROM level_progress")
    fun getAllLevelProgress(): Flow<List<LevelProgressEntity>>

    @Query("SELECT * FROM level_progress WHERE levelId = :levelId")
    suspend fun getLevelProgress(levelId: Int): LevelProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLevelProgress(progress: LevelProgressEntity)

    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfileEntity)

    @Query("SELECT * FROM discovered_words ORDER BY discoveredAtTimestamp DESC")
    fun getAllDiscoveredWords(): Flow<List<DiscoveredWordEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDiscoveredWord(word: DiscoveredWordEntity)

    @Query("SELECT COUNT(*) FROM discovered_words")
    suspend fun getDiscoveredWordsCount(): Int
}
