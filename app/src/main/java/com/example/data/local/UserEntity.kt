package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val email: String,
  val name: String,
  val passwordHash: String,
  val avatarIndex: Int = 0,
  val isRemembered: Boolean = true,
  val isLoggedIn: Boolean = false,
  val tier: String = "Ultra Pro AI",
  val createdAt: Long = System.currentTimeMillis()
)
