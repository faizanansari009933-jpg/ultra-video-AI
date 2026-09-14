package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [ProjectEntity::class, UserEntity::class],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun projectDao(): ProjectDao
  abstract fun userDao(): UserDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "ultra_video_ai.db"
        )
        .addCallback(object : Callback() {
          override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Seed initial default user and sample projects
            CoroutineScope(Dispatchers.IO).launch {
              val database = getInstance(context)
              val userDao = database.userDao()
              val projectDao = database.projectDao()

              // Create default account for Faizan Ansari (creator)
              userDao.insertUser(
                UserEntity(
                  email = "faizanansari009933@gmail.com",
                  name = "Faizan Ansari",
                  passwordHash = "UltraVideo2026!",
                  avatarIndex = 0,
                  isRemembered = true,
                  isLoggedIn = true,
                  tier = "Ultra Pro AI Max"
                )
              )

              // Seed sample projects
              projectDao.insertProject(
                ProjectEntity(
                  title = "Cyberpunk Neo Tokyo 8K",
                  resolution = "8K Ultra HD",
                  durationSeconds = 48,
                  thumbnailId = 1,
                  hasUnsavedChanges = false,
                  superResScale = "8K",
                  denoiseLevel = 0.85f,
                  deblurEnabled = true,
                  faceRestoration = true,
                  colorImprovement = 0.90f
                )
              )
              projectDao.insertProject(
                ProjectEntity(
                  title = "Cinematic Portrait Restoration",
                  resolution = "4K Ultra HD",
                  durationSeconds = 24,
                  thumbnailId = 2,
                  hasUnsavedChanges = false,
                  superResScale = "4K",
                  faceRestoration = true,
                  detailEnhanceLevel = 0.95f
                )
              )
              projectDao.insertProject(
                ProjectEntity(
                  title = "Golden Hour Drone Sequence",
                  resolution = "1080p Full HD",
                  durationSeconds = 62,
                  thumbnailId = 3,
                  hasUnsavedChanges = false,
                  videoSpeed = 1.25f,
                  colorImprovement = 0.85f
                )
              )
            }
          }
        })
        .fallbackToDestructiveMigration()
        .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
