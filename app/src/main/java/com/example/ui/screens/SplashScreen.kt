package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  onAnimationFinished: () -> Unit
) {
  var progress by remember { mutableFloatStateOf(0f) }

  // Modern entrance and looping animations
  val infiniteTransition = rememberInfiniteTransition(label = "splash_transition")
  val rotationRing by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(4000, easing = LinearEasing)
    ),
    label = "rotation"
  )

  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.95f,
    targetValue = 1.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  LaunchedEffect(Unit) {
    // Smooth progress simulation
    for (i in 1..20) {
      delay(90)
      progress = i / 20f
    }
    delay(400)
    onAnimationFinished()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            ObsidianBlack,
            Color(0xFF0F1220),
            ObsidianBlack
          )
        )
      ),
    contentAlignment = Alignment.Center
  ) {
    // Ambient background particle glow
    Canvas(modifier = Modifier.fillMaxSize()) {
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(NeonViolet.copy(alpha = 0.25f), Color.Transparent),
          center = Offset(size.width * 0.5f, size.height * 0.45f),
          radius = size.width * 0.65f
        ),
        radius = size.width * 0.65f,
        center = Offset(size.width * 0.5f, size.height * 0.45f)
      )
    }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // Animated Camera & AI Emblem
      Box(
        modifier = Modifier
          .size(140.dp)
          .scale(pulseScale),
        contentAlignment = Alignment.Center
      ) {
        // Outer rotating neon neural ring
        Canvas(
          modifier = Modifier
            .size(130.dp)
            .rotate(rotationRing)
        ) {
          drawCircle(
            brush = Brush.sweepGradient(
              colors = listOf(
                NeonViolet,
                NeonCyan,
                NeonEmerald,
                NeonViolet
              )
            ),
            style = Stroke(width = 3.dp.toPx())
          )
        }

        // Inner glowing core
        Box(
          modifier = Modifier
            .size(90.dp)
            .clip(CircleShape)
            .background(
              Brush.linearGradient(
                colors = listOf(
                  NeonViolet.copy(alpha = 0.8f),
                  Color(0xFF1F1235)
                )
              )
            )
            .border(2.dp, NeonCyan.copy(alpha = 0.6f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = "Ultra Video AI",
            tint = NeonCyan,
            modifier = Modifier.size(44.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(32.dp))

      // App Title
      Text(
        text = "Ultra Video AI",
        style = MaterialTheme.typography.headlineLarge.copy(
          fontWeight = FontWeight.ExtraBold,
          color = TextPrimary,
          letterSpacing = 1.5.sp
        ),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Subtitle
      Text(
        text = "AI Video Enhancer & Editor",
        style = MaterialTheme.typography.titleMedium.copy(
          color = NeonCyan,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 1.sp
        ),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(36.dp))

      // Progress bar
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .width(220.dp)
          .height(5.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = NeonViolet,
        trackColor = DarkSurfaceBorder
      )

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "Neural Rendering Engine Initializing...",
        style = MaterialTheme.typography.labelSmall.copy(
          color = TextSecondary,
          fontFamily = FontFamily.Monospace,
          fontSize = 11.sp
        )
      )
    }

    // Bottom Branding Notice
    Column(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 36.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0x22151824),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
      ) {
        Text(
          text = "Made by Faizan Ansari",
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
          style = MaterialTheme.typography.labelMedium.copy(
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        )
      }
    }
  }
}
