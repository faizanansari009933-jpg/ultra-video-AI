package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.FaceInfo
import com.example.domain.model.LogoConfig
import com.example.domain.model.TextLayer
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRose
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun VideoCanvasPreview(
  modifier: Modifier = Modifier,
  aspectRatioString: String = "16:9",
  resolutionLabel: String = "4K Ultra HD",
  durationSec: Int = 30,
  currentPlayTimeSec: Float = 0f,
  onPlayTimeChange: (Float) -> Unit = {},
  isPlaying: Boolean = false,
  onTogglePlay: () -> Unit = {},
  showSplitCompare: Boolean = false,
  splitPosition: Float = 0.5f,
  onSplitPositionChange: (Float) -> Unit = {},
  textLayers: List<TextLayer> = emptyList(),
  logoConfig: LogoConfig? = null,
  showFaceTracking: Boolean = false,
  detectedFaces: List<FaceInfo> = emptyList(),
  videoSpeed: Float = 1.0f,
  isMuted: Boolean = false,
  onToggleMute: () -> Unit = {},
  rotationDegrees: Int = 0
) {
  val ratioValue = when (aspectRatioString) {
    "9:16" -> 9f / 16f
    "1:1" -> 1f
    "4:5" -> 4f / 5f
    "21:9" -> 21f / 9f
    else -> 16f / 9f
  }

  // Animation pulse for AI particles
  val infiniteTransition = rememberInfiniteTransition(label = "canvas_anim")
  val pulseGlow by infiniteTransition.animateFloat(
    initialValue = 0.3f,
    targetValue = 0.85f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  val waveOffset by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 100f,
    animationSpec = infiniteRepeatable(
      animation = tween(2000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "wave"
  )

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .background(DarkSurface)
      .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(18.dp))
      .padding(12.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Top Canvas Meta Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(if (isPlaying) NeonEmerald else NeonAmber)
        )
        Text(
          text = if (isPlaying) "AI LIVE PREVIEW" else "PAUSED",
          style = MaterialTheme.typography.labelSmall.copy(
            color = if (isPlaying) NeonEmerald else TextSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        )
      }

      Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = NeonViolet.copy(alpha = 0.18f),
          border = androidx.compose.foundation.BorderStroke(1.dp, NeonViolet.copy(alpha = 0.4f))
        ) {
          Text(
            text = resolutionLabel,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall.copy(
              color = NeonCyan,
              fontWeight = FontWeight.Bold
            )
          )
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = DarkSurfaceBorder
        ) {
          Text(
            text = "${videoSpeed}x",
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall.copy(
              color = TextPrimary,
              fontWeight = FontWeight.Medium
            )
          )
        }
      }
    }

    // Video Viewport Canvas
    BoxWithConstraints(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(ratioValue)
        .clip(RoundedCornerShape(12.dp))
        .background(DarkCanvas)
        .border(1.dp, DarkSurfaceBorder.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
      contentAlignment = Alignment.Center
    ) {
      val canvasWidth = constraints.maxWidth.toFloat()
      val canvasHeight = constraints.maxHeight.toFloat()

      // Procedural Cinematic Background rendering (Simulating the active video frame)
      Canvas(
        modifier = Modifier
          .fillMaxSize()
          .rotate(rotationDegrees.toFloat())
      ) {
        val w = size.width
        val h = size.height

        // Background gradient
        drawRect(
          brush = Brush.radialGradient(
            colors = listOf(
              Color(0xFF1B1834),
              Color(0xFF0D101C),
              Color(0xFF07080E)
            ),
            center = Offset(w * 0.5f, h * 0.45f),
            radius = w * 0.8f
          )
        )

        // Simulated video scene elements: horizon, cyberpunk lights, AI grid
        val gridSpacing = 40f
        for (x in 0..(w / gridSpacing).toInt()) {
          drawLine(
            color = Color(0xFF262C45).copy(alpha = 0.35f),
            start = Offset(x * gridSpacing, h * 0.6f),
            end = Offset(x * gridSpacing + (x - (w / gridSpacing) / 2) * 20f, h),
            strokeWidth = 1f
          )
        }

        // Horizon line & light burst
        drawLine(
          color = NeonCyan.copy(alpha = 0.5f),
          start = Offset(0f, h * 0.6f),
          end = Offset(w, h * 0.6f),
          strokeWidth = 2f
        )

        // Central visual focal point (subject / glowing orb or portrait)
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(NeonViolet.copy(alpha = pulseGlow * 0.9f), Color.Transparent),
            center = Offset(w * 0.5f, h * 0.45f),
            radius = w * 0.32f
          ),
          radius = w * 0.32f,
          center = Offset(w * 0.5f, h * 0.45f)
        )

        // Waveform / video audio visualizer curve
        val wavePoints = 20
        for (i in 0 until wavePoints) {
          val barW = w / (wavePoints * 2f)
          val barH = (Math.sin((i + currentPlayTimeSec * 4).toDouble()) * 25 + 35).toFloat() * pulseGlow
          drawRoundRect(
            color = if (i % 2 == 0) NeonCyan.copy(alpha = 0.8f) else NeonViolet.copy(alpha = 0.8f),
            topLeft = Offset(w * 0.25f + i * (barW * 2f), h * 0.72f - barH / 2),
            size = Size(barW, barH),
            cornerRadius = CornerRadius(4f, 4f)
          )
        }

        // If in Split Compare mode: draw the Before side with lower contrast & pixel noise simulation
        if (showSplitCompare) {
          val splitX = w * splitPosition
          // Left side: "Before: Raw 720p" (darker, slightly desaturated)
          drawRect(
            color = Color.Black.copy(alpha = 0.25f),
            topLeft = Offset.Zero,
            size = Size(splitX, h)
          )

          // Split divider line
          drawLine(
            color = NeonCyan,
            start = Offset(splitX, 0f),
            end = Offset(splitX, h),
            strokeWidth = 3.dp.toPx()
          )
        }
      }

      // Split compare labels and drag handle
      if (showSplitCompare) {
        // Draggable handle
        Box(
          modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
              detectDragGestures { change, dragAmount ->
                change.consume()
                val newPos = (splitPosition + dragAmount.x / canvasWidth).coerceIn(0.05f, 0.95f)
                onSplitPositionChange(newPos)
              }
            }
        ) {
          // Handle pill
          Box(
            modifier = Modifier
              .align(Alignment.CenterStart)
              .offset { IntOffset((canvasWidth * splitPosition - 16.dp.toPx()).roundToInt(), 0) }
              .size(32.dp)
              .clip(CircleShape)
              .background(NeonCyan)
              .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Compare,
              contentDescription = "Slide to Compare",
              tint = Color.Black,
              modifier = Modifier.size(18.dp)
            )
          }

          // Before tag (left)
          Surface(
            modifier = Modifier
              .align(Alignment.TopStart)
              .padding(8.dp),
            shape = RoundedCornerShape(6.dp),
            color = Color.Black.copy(alpha = 0.65f)
          ) {
            Text(
              text = "BEFORE (Raw)",
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
              style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp)
            )
          }

          // After tag (right)
          Surface(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(8.dp),
            shape = RoundedCornerShape(6.dp),
            color = NeonViolet.copy(alpha = 0.75f)
          ) {
            Text(
              text = "AFTER ($resolutionLabel AI)",
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
              style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp)
            )
          }
        }
      }

      // Face tracking overlay boxes
      if (showFaceTracking) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          for (face in detectedFaces) {
            val left = face.boxNormX * size.width
            val top = face.boxNormY * size.height
            val boxW = face.boxNormW * size.width
            val boxH = face.boxNormH * size.height

            // Glowing bounding box
            drawRoundRect(
              color = NeonCyan,
              topLeft = Offset(left, top),
              size = Size(boxW, boxH),
              cornerRadius = CornerRadius(8f, 8f),
              style = Stroke(
                width = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
              )
            )

            // Landmarks (eyes and mouth)
            drawCircle(
              color = NeonEmerald,
              radius = 4.dp.toPx(),
              center = Offset(left + boxW * 0.32f, top + boxH * face.landmarkEyesY)
            )
            drawCircle(
              color = NeonEmerald,
              radius = 4.dp.toPx(),
              center = Offset(left + boxW * 0.68f, top + boxH * face.landmarkEyesY)
            )
            drawCircle(
              color = NeonAmber,
              radius = 4.dp.toPx(),
              center = Offset(left + boxW * 0.5f, top + boxH * face.landmarkMouthY)
            )
          }
        }

        // Face info floating badge
        if (detectedFaces.isNotEmpty()) {
          Surface(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .padding(8.dp),
            shape = RoundedCornerShape(6.dp),
            color = Color.Black.copy(alpha = 0.75f),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
          ) {
            Text(
              text = "👤 ${detectedFaces.size} Face(s) Detected • Natural Restoration ON",
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontSize = 11.sp)
            )
          }
        }
      }

      // Render Text Layers
      for (layer in textLayers) {
        if (currentPlayTimeSec >= layer.startSec && currentPlayTimeSec <= layer.endSec) {
          val font = when (layer.fontName) {
            "Monospace" -> FontFamily.Monospace
            "Serif" -> FontFamily.Serif
            "Cursive" -> FontFamily.Cursive
            else -> FontFamily.Default
          }

          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(12.dp)
          ) {
            Text(
              text = layer.text,
              fontFamily = font,
              fontSize = (layer.fontSize * 0.8f).sp,
              fontWeight = FontWeight.Bold,
              color = Color(layer.color).copy(alpha = layer.opacity),
              modifier = Modifier
                .align(
                  when {
                    layer.posY < 0.33f -> Alignment.TopCenter
                    layer.posY > 0.66f -> Alignment.BottomCenter
                    else -> Alignment.Center
                  }
                )
                .background(Color.Black.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }

      // Render Logo / Watermark (Uploaded Image or Generated Badge)
      logoConfig?.let { logo ->
        val isVisibleTime = logo.isPersistent || (currentPlayTimeSec >= logo.startSec && currentPlayTimeSec <= logo.endSec)
        if (logo.enabled && isVisibleTime) {
          val align = when (logo.cornerPreset) {
            "Top-Left" -> Alignment.TopStart
            "Bottom-Left" -> Alignment.BottomStart
            "Bottom-Right" -> Alignment.BottomEnd
            "Center" -> Alignment.Center
            else -> Alignment.TopEnd
          }

          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding((logo.edgeMarginDp.coerceIn(4, 48)).dp)
          ) {
            Box(
              modifier = Modifier
                .align(align)
                .rotate(logo.rotation)
            ) {
              if (logo.sourceType == "UPLOAD" && !logo.imageUri.isNullOrEmpty()) {
                // Uploaded Image Watermark
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = Color.Transparent
                ) {
                  coil.compose.AsyncImage(
                    model = logo.imageUri,
                    contentDescription = "Uploaded Watermark",
                    modifier = Modifier
                      .size((64f * logo.scale).dp)
                      .clip(RoundedCornerShape(8.dp)),
                    alpha = logo.opacity,
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit
                  )
                }
              } else {
                // Generated Text-Based / Insignia Logo Overlay
                val themeColor = Color(logo.colorThemeHex)
                val badgeShape = when (logo.backgroundStyle) {
                  "Transparent" -> RoundedCornerShape(6.dp)
                  "Neon Border" -> RoundedCornerShape(10.dp)
                  "Solid Slate" -> RoundedCornerShape(10.dp)
                  else -> RoundedCornerShape(20.dp) // Frosted Capsule
                }

                val badgeBgColor = when (logo.backgroundStyle) {
                  "Transparent" -> Color.Transparent
                  "Neon Border" -> DarkSurfaceElevated.copy(alpha = logo.opacity * 0.4f)
                  "Solid Slate" -> DarkSurface.copy(alpha = logo.opacity * 0.85f)
                  else -> Color(0xFF0F121C).copy(alpha = logo.opacity * 0.75f) // Frosted Capsule
                }

                val borderStroke = if (logo.backgroundStyle == "Transparent") {
                  null
                } else {
                  androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (logo.backgroundStyle == "Neon Border") themeColor.copy(alpha = logo.opacity)
                    else DarkSurfaceBorder.copy(alpha = logo.opacity)
                  )
                }

                Surface(
                  shape = badgeShape,
                  color = badgeBgColor,
                  border = borderStroke
                ) {
                  when (logo.generatorStyle) {
                    "Studio Monogram" -> {
                      val initials = logo.logoTitle.split(" ")
                        .filter { it.isNotBlank() }
                        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                        .take(2)
                        .joinToString("")
                        .ifEmpty { "UA" }

                      Row(
                        modifier = Modifier.padding(
                          horizontal = (8f * logo.scale).dp,
                          vertical = (5f * logo.scale).dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy((6f * logo.scale).dp)
                      ) {
                        Box(
                          modifier = Modifier
                            .size((22f * logo.scale).dp)
                            .clip(RoundedCornerShape((5f * logo.scale).dp))
                            .background(themeColor.copy(alpha = logo.opacity)),
                          contentAlignment = Alignment.Center
                        ) {
                          Text(
                            text = initials,
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = (11f * logo.scale).sp,
                            fontFamily = FontFamily.Monospace
                          )
                        }
                        Column {
                          Text(
                            text = logo.logoTitle,
                            color = Color.White.copy(alpha = logo.opacity),
                            fontWeight = FontWeight.Bold,
                            fontSize = (10f * logo.scale).sp,
                            letterSpacing = 0.5.sp
                          )
                          if (logo.tagline.isNotBlank()) {
                            Text(
                              text = logo.tagline,
                              color = themeColor.copy(alpha = logo.opacity * 0.85f),
                              fontWeight = FontWeight.Medium,
                              fontSize = (8f * logo.scale).sp
                            )
                          }
                        }
                      }
                    }

                    "Clean Watermark" -> {
                      Row(
                        modifier = Modifier.padding(
                          horizontal = (8f * logo.scale).dp,
                          vertical = (4f * logo.scale).dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy((4f * logo.scale).dp)
                      ) {
                        Text(
                          text = "©",
                          color = themeColor.copy(alpha = logo.opacity),
                          fontWeight = FontWeight.Bold,
                          fontSize = (12f * logo.scale).sp
                        )
                        Text(
                          text = if (logo.tagline.isNotBlank()) "${logo.logoTitle} • ${logo.tagline}" else logo.logoTitle,
                          color = Color.White.copy(alpha = logo.opacity),
                          fontWeight = FontWeight.SemiBold,
                          fontSize = (10f * logo.scale).sp,
                          letterSpacing = 1.sp
                        )
                      }
                    }

                    "Cinematic Serif" -> {
                      Column(
                        modifier = Modifier.padding(
                          horizontal = (10f * logo.scale).dp,
                          vertical = (5f * logo.scale).dp
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                      ) {
                        Text(
                          text = logo.logoTitle.uppercase(),
                          color = themeColor.copy(alpha = logo.opacity),
                          fontWeight = FontWeight.ExtraBold,
                          fontSize = (11f * logo.scale).sp,
                          letterSpacing = 2.sp,
                          fontFamily = FontFamily.Serif
                        )
                        if (logo.tagline.isNotBlank()) {
                          Box(
                            modifier = Modifier
                              .width((30f * logo.scale).dp)
                              .height(1.dp)
                              .background(themeColor.copy(alpha = logo.opacity * 0.6f))
                          )
                          Text(
                            text = logo.tagline.uppercase(),
                            color = Color.White.copy(alpha = logo.opacity * 0.8f),
                            fontWeight = FontWeight.Normal,
                            fontSize = (7f * logo.scale).sp,
                            letterSpacing = 1.5.sp,
                            fontFamily = FontFamily.Serif
                          )
                        }
                      }
                    }

                    "Minimalist Stamp" -> {
                      Row(
                        modifier = Modifier.padding(
                          horizontal = (6f * logo.scale).dp,
                          vertical = (3f * logo.scale).dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy((4f * logo.scale).dp)
                      ) {
                        Box(
                          modifier = Modifier
                            .size((5f * logo.scale).dp)
                            .clip(CircleShape)
                            .background(themeColor.copy(alpha = logo.opacity))
                        )
                        Text(
                          text = logo.logoTitle,
                          color = Color.White.copy(alpha = logo.opacity),
                          fontWeight = FontWeight.Medium,
                          fontSize = (9f * logo.scale).sp,
                          letterSpacing = 0.8.sp
                        )
                      }
                    }

                    else -> { // "Cyber Badge" / "Glassmorphic Tag"
                      Row(
                        modifier = Modifier.padding(
                          horizontal = (10f * logo.scale).dp,
                          vertical = (5f * logo.scale).dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy((6f * logo.scale).dp)
                      ) {
                        Box(
                          modifier = Modifier
                            .size((10f * logo.scale).dp)
                            .clip(CircleShape)
                            .background(themeColor.copy(alpha = logo.opacity))
                        )
                        Column {
                          Text(
                            text = logo.logoTitle,
                            color = Color.White.copy(alpha = logo.opacity),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = (11f * logo.scale).sp,
                            letterSpacing = 1.sp
                          )
                          if (logo.tagline.isNotBlank()) {
                            Text(
                              text = logo.tagline,
                              color = themeColor.copy(alpha = logo.opacity * 0.9f),
                              fontWeight = FontWeight.Bold,
                              fontSize = (8f * logo.scale).sp,
                              fontFamily = FontFamily.Monospace
                            )
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Scrub Slider & Timecode Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = formatTimecode(currentPlayTimeSec),
        style = MaterialTheme.typography.labelSmall.copy(
          color = NeonCyan,
          fontWeight = FontWeight.SemiBold,
          fontFamily = FontFamily.Monospace
        )
      )

      Slider(
        value = currentPlayTimeSec,
        onValueChange = onPlayTimeChange,
        valueRange = 0f..durationSec.toFloat().coerceAtLeast(1f),
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 8.dp)
          .testTag("video_scrub_slider"),
        colors = SliderDefaults.colors(
          thumbColor = NeonViolet,
          activeTrackColor = NeonViolet,
          inactiveTrackColor = DarkSurfaceBorder
        )
      )

      Text(
        text = formatTimecode(durationSec.toFloat()),
        style = MaterialTheme.typography.labelSmall.copy(
          color = TextSecondary,
          fontFamily = FontFamily.Monospace
        )
      )
    }

    // Media Control Buttons Row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 2.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onToggleMute,
        modifier = Modifier.size(38.dp)
      ) {
        Icon(
          imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
          contentDescription = if (isMuted) "Unmute Audio" else "Mute Audio",
          tint = if (isMuted) NeonRose else TextSecondary
        )
      }

      Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = { onPlayTimeChange((currentPlayTimeSec - 5f).coerceAtLeast(0f)) },
          modifier = Modifier.size(38.dp)
        ) {
          Icon(
            imageVector = Icons.Default.FastRewind,
            contentDescription = "Rewind 5s",
            tint = TextSecondary
          )
        }

        Surface(
          modifier = Modifier.size(46.dp),
          shape = CircleShape,
          color = NeonViolet,
          shadowElevation = 6.dp
        ) {
          IconButton(
            onClick = onTogglePlay,
            modifier = Modifier.testTag("play_pause_button")
          ) {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = if (isPlaying) "Pause Video" else "Play Video",
              tint = Color.White,
              modifier = Modifier.size(26.dp)
            )
          }
        }

        IconButton(
          onClick = { onPlayTimeChange((currentPlayTimeSec + 5f).coerceAtMost(durationSec.toFloat())) },
          modifier = Modifier.size(38.dp)
        ) {
          Icon(
            imageVector = Icons.Default.FastForward,
            contentDescription = "Forward 5s",
            tint = TextSecondary
          )
        }
      }

      IconButton(
        onClick = { onPlayTimeChange(0f) },
        modifier = Modifier.size(38.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Refresh,
          contentDescription = "Restart Video",
          tint = TextSecondary
        )
      }
    }
  }
}

private fun formatTimecode(seconds: Float): String {
  val totalSecs = seconds.toInt()
  val mins = totalSecs / 60
  val secs = totalSecs % 60
  val millis = ((seconds - totalSecs) * 100).toInt().coerceIn(0, 99)
  return String.format("%02d:%02d.%02d", mins, secs, millis)
}
