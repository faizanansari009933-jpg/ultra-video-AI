package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRose
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.roundToInt

@Composable
fun TimelineScrubber(
  modifier: Modifier = Modifier,
  durationSec: Int = 30,
  currentPlayTimeSec: Float = 0f,
  onPlayTimeChange: (Float) -> Unit = {},
  trimStartSec: Float = 0f,
  trimEndSec: Float = 30f,
  splitPoints: List<Float> = emptyList(),
  hasTextTrack: Boolean = true,
  hasLogoTrack: Boolean = true
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(DarkSurfaceElevated)
      .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp))
      .padding(12.dp)
  ) {
    // Header with tracks info & zoom
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Layers,
          contentDescription = "Multi-track Timeline",
          tint = NeonCyan,
          modifier = Modifier.size(16.dp)
        )
        Text(
          text = "TIMELINE • MULTI-TRACK",
          style = MaterialTheme.typography.labelSmall.copy(
            color = TextSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        )
      }

      Text(
        text = "${durationSec}s Total",
        style = MaterialTheme.typography.labelSmall.copy(
          color = NeonViolet,
          fontWeight = FontWeight.SemiBold,
          fontFamily = FontFamily.Monospace
        )
      )
    }

    // Ruler + Tracks Box
    BoxWithConstraints(
      modifier = Modifier
        .fillMaxWidth()
        .height(130.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(DarkCanvas)
        .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(10.dp))
    ) {
      val timelineWidth = constraints.maxWidth.toFloat()
      val durationFloat = durationSec.toFloat().coerceAtLeast(1f)

      // Time ruler & track backgrounds
      Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Top ruler (24dp height)
        val rulerH = 22.dp.toPx()
        drawRect(
          color = Color(0xFF141724),
          topLeft = Offset.Zero,
          size = Size(w, rulerH)
        )

        // Second ticks
        val numTicks = (durationSec / 5).coerceAtLeast(1)
        for (i in 0..numTicks) {
          val tickX = (i * 5f / durationFloat) * w
          drawLine(
            color = TextSecondary.copy(alpha = 0.5f),
            start = Offset(tickX, 0f),
            end = Offset(tickX, rulerH * 0.5f),
            strokeWidth = 1f
          )
        }

        // Track 1: Video Track (height ~34dp)
        val t1Y = rulerH + 4.dp.toPx()
        val t1H = 32.dp.toPx()
        val trimStartX = (trimStartSec / durationFloat) * w
        val trimEndX = (trimEndSec / durationFloat) * w

        // Inactive before trimStart
        drawRect(
          color = Color(0xFF1B1F30),
          topLeft = Offset(0f, t1Y),
          size = Size(trimStartX, t1H)
        )
        // Active video clip track
        drawRoundRect(
          color = NeonViolet.copy(alpha = 0.75f),
          topLeft = Offset(trimStartX, t1Y),
          size = Size(trimEndX - trimStartX, t1H),
          cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
        )
        // Inactive after trimEnd
        drawRect(
          color = Color(0xFF1B1F30),
          topLeft = Offset(trimEndX, t1Y),
          size = Size(w - trimEndX, t1H)
        )

        // Split cuts
        for (split in splitPoints) {
          val splitX = (split / durationFloat) * w
          drawLine(
            color = NeonRose,
            start = Offset(splitX, t1Y),
            end = Offset(splitX, t1Y + t1H),
            strokeWidth = 2.dp.toPx()
          )
        }

        // Track 2: Text Layers Track (height ~24dp)
        val t2Y = t1Y + t1H + 4.dp.toPx()
        val t2H = 24.dp.toPx()
        if (hasTextTrack) {
          drawRoundRect(
            color = NeonCyan.copy(alpha = 0.6f),
            topLeft = Offset(w * 0.1f, t2Y),
            size = Size(w * 0.5f, t2H),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
          )
        }

        // Track 3: Logo / Watermark Track (height ~24dp)
        val t3Y = t2Y + t2H + 4.dp.toPx()
        val t3H = 24.dp.toPx()
        if (hasLogoTrack) {
          drawRoundRect(
            color = NeonAmber.copy(alpha = 0.6f),
            topLeft = Offset(0f, t3Y),
            size = Size(w * 0.85f, t3H),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
          )
        }
      }

      // Track Labels inside tracks
      Column(
        modifier = Modifier
          .fillMaxHeight()
          .padding(start = 6.dp, top = 26.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Icon(Icons.Default.Movie, contentDescription = "Video Track", tint = Color.White, modifier = Modifier.size(14.dp))
          Text("Video Clip (Main)", style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 10.sp))
        }
        if (hasTextTrack) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.TextFields, contentDescription = "Text Track", tint = NeonCyan, modifier = Modifier.size(13.dp))
            Text("Text Layer Track", style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 10.sp))
          }
        }
        if (hasLogoTrack) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.WaterDrop, contentDescription = "Logo Track", tint = NeonAmber, modifier = Modifier.size(13.dp))
            Text("Logo / Watermark", style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 10.sp))
          }
        }
      }

      // Playhead vertical scrubber line
      val playheadNorm = (currentPlayTimeSec / durationFloat).coerceIn(0f, 1f)
      val playheadX = playheadNorm * timelineWidth

      Box(
        modifier = Modifier
          .fillMaxSize()
          .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
              change.consume()
              val newSec = ((currentPlayTimeSec / durationFloat + dragAmount.x / timelineWidth) * durationFloat)
                .coerceIn(0f, durationFloat)
              onPlayTimeChange(newSec)
            }
          }
      ) {
        // Red / Neon Playhead bar
        Box(
          modifier = Modifier
            .offset { IntOffset((playheadX - 1.dp.toPx()).roundToInt(), 0) }
            .width(2.dp)
            .fillMaxHeight()
            .background(NeonRose)
        )
        // Playhead head indicator at top
        Box(
          modifier = Modifier
            .offset { IntOffset((playheadX - 8.dp.toPx()).roundToInt(), 0) }
            .size(16.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(NeonRose),
          contentAlignment = Alignment.Center
        ) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .clip(CircleShape)
              .background(Color.White)
          )
        }
      }
    }
  }
}
