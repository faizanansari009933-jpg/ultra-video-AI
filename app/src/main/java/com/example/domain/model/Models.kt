package com.example.domain.model

import java.util.UUID

data class TextLayer(
  val id: String = UUID.randomUUID().toString(),
  val text: String = "ULTRA VIDEO AI",
  val fontName: String = "Futuristic",
  val fontSize: Float = 28f,
  val color: Long = 0xFFFFFFFF,
  val posX: Float = 0.5f, // 0.0 to 1.0 (center normalized)
  val posY: Float = 0.8f,
  val opacity: Float = 1.0f,
  val animation: String = "Fade In",
  val startSec: Float = 0f,
  val endSec: Float = 10f
)

data class LogoConfig(
  val enabled: Boolean = true,
  val sourceType: String = "GENERATED", // "UPLOAD" or "GENERATED"
  val imageUri: String? = null,
  val logoTitle: String = "ULTRA AI",
  val tagline: String = "OFFICIAL 4K",
  val generatorStyle: String = "Cyber Badge", // Cyber Badge, Studio Monogram, Clean Watermark, Cinematic Serif, Glassmorphic Tag, Minimalist Stamp
  val accentIcon: String = "Diamond", // Diamond, Crown, Shield, Camera, Sparkle, Copyright, Verified
  val colorThemeHex: Long = 0xFF00E5FF,
  val backgroundStyle: String = "Frosted Capsule", // Frosted Capsule, Transparent, Neon Border, Solid Slate
  val watermarkType: String = "Corner Badge",
  val scale: Float = 1.0f,
  val posX: Float = 0.82f,
  val posY: Float = 0.12f,
  val rotation: Float = 0f,
  val opacity: Float = 0.85f,
  val startSec: Float = 0f,
  val endSec: Float = 30f,
  val cornerPreset: String = "Top-Right",
  val edgeMarginDp: Int = 16,
  val isPersistent: Boolean = true
)

data class FaceInfo(
  val id: Int,
  val label: String,
  val confidence: Float,
  val boxNormX: Float,
  val boxNormY: Float,
  val boxNormW: Float,
  val boxNormH: Float,
  val landmarkEyesY: Float,
  val landmarkMouthY: Float,
  val isTracked: Boolean = true
)

data class ExportSettings(
  val resolution: String = "4K Ultra HD",
  val fps: Int = 60,
  val bitrate: String = "High (35 Mbps)",
  val format: String = "MP4 (H.265 HEVC)",
  val audioPreserved: Boolean = true,
  val audioFormat: String = "AAC 320 kbps Studio"
)

enum class ScreenRoute {
  SPLASH,
  AUTH,
  HOME,
  AI_ENHANCE,
  VIDEO_EDITOR,
  FACE_TOOLS,
  ADD_LOGO,
  ADD_TEXT,
  MY_PROJECTS,
  PROFILE,
  SETTINGS,
  EXPORT
}
