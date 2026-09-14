package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
  @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
  fun getActiveUserFlow(): Flow<UserEntity?>

  @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
  suspend fun getActiveUser(): UserEntity?

  @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
  suspend fun findUserByEmail(email: String): UserEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(user: UserEntity): Long

  @Update
  suspend fun updateUser(user: UserEntity)

  @Query("UPDATE users SET isLoggedIn = 0")
  suspend fun logoutAll()

  @Query("UPDATE users SET isLoggedIn = 1, isRemembered = :remember WHERE id = :id")
  suspend fun setActiveUser(id: Long, remember: Boolean)

  @Query("UPDATE users SET passwordHash = :newPassword WHERE LOWER(email) = LOWER(:email)")
  suspend fun resetPassword(email: String, newPassword: String): Int
}
