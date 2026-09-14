package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val UltraDarkColorScheme = darkColorScheme(
  primary = NeonViolet,
  onPrimary = TextPrimary,
  primaryContainer = DarkSurfaceElevated,
  onPrimaryContainer = NeonCyan,
  secondary = NeonCyan,
  onSecondary = ObsidianBlack,
  secondaryContainer = DarkSurfaceBorder,
  onSecondaryContainer = NeonCyan,
  tertiary = NeonAmber,
  onTertiary = ObsidianBlack,
  background = ObsidianBlack,
  onBackground = TextPrimary,
  surface = DarkSurface,
  onSurface = TextPrimary,
  surfaceVariant = DarkSurfaceElevated,
  onSurfaceVariant = TextSecondary,
  outline = DarkSurfaceBorder,
  outlineVariant = DarkCanvas,
  error = NeonRose,
  onError = TextPrimary
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force premium dark theme by default as per request
  content: @Composable () -> Unit
) {
  val colorScheme = UltraDarkColorScheme
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as? Activity)?.window
      if (window != null) {
        window.statusBarColor = ObsidianBlack.toArgb()
        window.navigationBarColor = ObsidianBlack.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
      }
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
