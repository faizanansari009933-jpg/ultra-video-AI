package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val title: String,
  val resolution: String = "4K Ultra HD",
  val durationSeconds: Int = 30,
  val lastModified: Long = System.currentTimeMillis(),
  val thumbnailId: Int = 1,
  val hasUnsavedChanges: Boolean = false,
  
  // Enhancement parameters
  val denoiseLevel: Float = 0.75f,
  val deblurEnabled: Boolean = true,
  val sharpenLevel: Float = 0.65f,
  val superResScale: String = "4K",
  val faceRestoration: Boolean = true,
  val detailEnhanceLevel: Float = 0.80f,
  val colorImprovement: Float = 0.70f,
  val lightingImprovement: Float = 0.60f,
  val identityPreservation: Float = 0.95f,
  
  // Editor parameters
  val videoSpeed: Float = 1.0f,
  val isMuted: Boolean = false,
  val volume: Float = 1.0f,
  val rotationDegrees: Int = 0,
  val aspectRatio: String = "16:9",
  val trimStartSec: Float = 0f,
  val trimEndSec: Float = 30f,
  
  // Text and Logo count
  val textCount: Int = 0,
  val hasLogo: Boolean = false
)
