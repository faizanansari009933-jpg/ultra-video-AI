package com.example.data.repository

import com.example.data.local.UserDao
import com.example.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

class AuthRepository(private val userDao: UserDao) {
  val activeUser: Flow<UserEntity?> = userDao.getActiveUserFlow()

  suspend fun login(email: String, password: String,remember: Boolean): Result<UserEntity> {
    val user = userDao.findUserByEmail(email.trim())
    return if (user != null && user.passwordHash == password) {
      userDao.logoutAll()
      userDao.setActiveUser(user.id, remember)
      Result.success(user.copy(isLoggedIn = true, isRemembered = remember))
    } else if (user != null) {
      Result.failure(Exception("Invalid password. Please verify and try again."))
    } else {
      Result.failure(Exception("Account not found with $email. Please sign up."))
    }
  }

  suspend fun googleSignIn(accountEmail: String, accountName: String): Result<UserEntity> {
    val existing = userDao.findUserByEmail(accountEmail.trim())
    val userId = if (existing != null) {
      userDao.logoutAll()
      userDao.setActiveUser(existing.id, true)
      existing.id
    } else {
      userDao.logoutAll()
      val newId = userDao.insertUser(
        UserEntity(
          email = accountEmail.trim(),
          name = accountName,
          passwordHash = "google_oauth_token",
          avatarIndex = 0,
          isRemembered = true,
          isLoggedIn = true,
          tier = "Ultra Pro AI Max"
        )
      )
      newId
    }
    val loggedIn = userDao.getActiveUser()
    return if (loggedIn != null) Result.success(loggedIn) else Result.failure(Exception("Google Sign-In failed"))
  }

  suspend fun signUp(name: String, email: String, password: String, remember: Boolean): Result<UserEntity> {
    val existing = userDao.findUserByEmail(email.trim())
    if (existing != null) {
      return Result.failure(Exception("An account with this email already exists."))
    }
    userDao.logoutAll()
    val newId = userDao.insertUser(
      UserEntity(
        email = email.trim(),
        name = name.trim(),
        passwordHash = password,
        avatarIndex = (0..4).random(),
        isRemembered = remember,
        isLoggedIn = true,
        tier = "Ultra Pro AI"
      )
    )
    val user = userDao.getActiveUser()
    return if (user != null) Result.success(user) else Result.failure(Exception("Sign up failed."))
  }

  suspend fun resetPassword(email: String, newPassword: String): Result<Boolean> {
    val count = userDao.resetPassword(email.trim(), newPassword)
    return if (count > 0) {
      Result.success(true)
    } else {
      Result.failure(Exception("Email not found. Please register first."))
    }
  }

  suspend fun logout() {
    userDao.logoutAll()
  }

  suspend fun updateProfile(user: UserEntity) {
    userDao.updateUser(user)
  }
}
