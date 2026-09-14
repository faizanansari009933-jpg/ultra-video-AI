package com.example.ui.screens.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TimelineScrubber
import com.example.ui.components.UltraTopAppBar
import com.example.ui.components.VideoCanvasPreview
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
import com.example.ui.viewmodel.UltraVideoViewModel
import kotlin.math.roundToInt

@Composable
fun EditorScreen(
  viewModel: UltraVideoViewModel,
  onBackClick: () -> Unit
) {
  val activeProject by viewModel.activeProject.collectAsState()
  val isPlaying by viewModel.isPlaying.collectAsState()
  val currentTime by viewModel.currentTimeSec.collectAsState()
  val duration = activeProject.durationSeconds

  val videoSpeed by viewModel.videoSpeed.collectAsState()
  val isMuted by viewModel.isMuted.collectAsState()
  val rotationDeg by viewModel.rotationDegrees.collectAsState()
  val aspectRatio by viewModel.aspectRatio.collectAsState()
  val trimStart by viewModel.trimStartSec.collectAsState()
  val trimEnd by viewModel.trimEndSec.collectAsState()
  val splitPoints by viewModel.splitPoints.collectAsState()

  val canUndo by viewModel.canUndo.collectAsState()
  val canRedo by viewModel.canRedo.collectAsState()

  val speeds = listOf(0.25f, 0.5f, 1.0f, 1.5f, 2.0f, 4.0f)
  val aspectRatios = listOf("16:9", "9:16", "1:1", "4:5", "21:9")

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DarkCanvas)
  ) {
    UltraTopAppBar(
      title = "Video Editor",
      onBackClick = onBackClick,
      actions = {
        // Undo button
        IconButton(
          onClick = { viewModel.undo() },
          enabled = canUndo,
          modifier = Modifier.testTag("editor_undo_button")
        ) {
          Icon(
            Icons.Default.Undo,
            contentDescription = "Undo",
            tint = if (canUndo) NeonCyan else TextSecondary.copy(alpha = 0.4f)
          )
        }

        // Redo button
        IconButton(
          onClick = { viewModel.redo() },
          enabled = canRedo,
          modifier = Modifier.testTag("editor_redo_button")
        ) {
          Icon(
            Icons.Default.Redo,
            contentDescription = "Redo",
            tint = if (canRedo) NeonCyan else TextSecondary.copy(alpha = 0.4f)
          )
        }

        // Save Project button
        Button(
          onClick = { viewModel.saveCurrentProject() },
          colors = ButtonDefaults.buttonColors(containerColor = NeonViolet.copy(alpha = 0.25f)),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .padding(end = 8.dp)
            .testTag("editor_save_button")
        ) {
          Icon(Icons.Default.Save, contentDescription = "Save", tint = NeonCyan, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Save", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Large Video Preview
      item {
        VideoCanvasPreview(
          aspectRatioString = aspectRatio,
          resolutionLabel = activeProject.resolution,
          durationSec = duration,
          currentPlayTimeSec = currentTime,
          onPlayTimeChange = { viewModel.setPlayTime(it) },
          isPlaying = isPlaying,
          onTogglePlay = { viewModel.togglePlay() },
          showSplitCompare = false,
          videoSpeed = videoSpeed,
          isMuted = isMuted,
          onToggleMute = { viewModel.toggleMute() },
          rotationDegrees = rotationDeg
        )
      }

      // Multi-Track Timeline
      item {
        TimelineScrubber(
          durationSec = duration,
          currentPlayTimeSec = currentTime,
          onPlayTimeChange = { viewModel.setPlayTime(it) },
          trimStartSec = trimStart,
          trimEndSec = trimEnd,
          splitPoints = splitPoints,
          hasTextTrack = viewModel.textLayers.collectAsState().value.isNotEmpty(),
          hasLogoTrack = viewModel.logoConfig.collectAsState().value.enabled
        )
      }

      // Quick Cut & Split Action Bar
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = { viewModel.cutSplitAtPlayhead() },
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("cut_split_button"),
            colors = ButtonDefaults.buttonColors(containerColor = NeonRose),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.ContentCut, contentDescription = "Cut", tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Split at Playhead", fontWeight = FontWeight.Bold)
          }

          Button(
            onClick = { viewModel.rotateVideo() },
            modifier = Modifier
              .weight(0.7f)
              .height(48.dp)
              .testTag("rotate_video_button"),
            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.RotateRight, contentDescription = "Rotate", tint = NeonCyan, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Rotate (${rotationDeg}°)", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
          }
        }
      }

      // Trim Range Control Card
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Clip Trim Bounds", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
              Text(
                text = "${trimStart.toInt()}s — ${trimEnd.toInt()}s (${(trimEnd - trimStart).toInt()}s total)",
                style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            RangeSlider(
              value = trimStart..trimEnd,
              onValueChange = { range ->
                viewModel.updateTrim(range.start, range.endInclusive)
              },
              valueRange = 0f..duration.toFloat(),
              colors = SliderDefaults.colors(
                thumbColor = NeonCyan,
                activeTrackColor = NeonViolet,
                inactiveTrackColor = DarkSurfaceBorder
              )
            )
          }
        }
      }

      // Crop & Aspect Ratio Selector
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(Icons.Default.Crop, contentDescription = "Crop", tint = NeonAmber, modifier = Modifier.size(18.dp))
              Text("Aspect Ratio & Crop", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(aspectRatios) { ratio ->
                val isSelected = aspectRatio == ratio
                Surface(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { viewModel.setAspectRatio(ratio) }
                    .testTag("aspect_ratio_$ratio"),
                  shape = RoundedCornerShape(8.dp),
                  color = if (isSelected) NeonAmber else DarkSurfaceElevated,
                  border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NeonAmber else DarkSurfaceBorder)
                ) {
                  Text(
                    text = when (ratio) {
                      "16:9" -> "16:9 (Landscape)"
                      "9:16" -> "9:16 (Reels/Shorts)"
                      "1:1" -> "1:1 (Square)"
                      "4:5" -> "4:5 (Portrait)"
                      "21:9" -> "21:9 (Cinema)"
                      else -> ratio
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = if (isSelected) Color.Black else TextPrimary,
                      fontWeight = FontWeight.Bold
                    )
                  )
                }
              }
            }
          }
        }
      }

      // Speed Control Strip
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(Icons.Default.Speed, contentDescription = "Speed", tint = NeonEmerald, modifier = Modifier.size(18.dp))
              Text("Playback Speed Control", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(speeds) { speed ->
                val isSelected = videoSpeed == speed
                Surface(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { viewModel.setSpeed(speed) }
                    .testTag("speed_option_${speed}x"),
                  shape = RoundedCornerShape(8.dp),
                  color = if (isSelected) NeonEmerald else DarkSurfaceElevated,
                  border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NeonEmerald else DarkSurfaceBorder)
                ) {
                  Text(
                    text = "${speed}x",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium.copy(
                      color = if (isSelected) Color.Black else TextPrimary,
                      fontWeight = FontWeight.Bold
                    )
                  )
                }
              }
            }
          }
        }
      }

      // Mute / Audio Track
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Audio Track & Volume", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
              Text(
                text = if (isMuted) "Audio is currently MUTED" else "Audio active (100%)",
                style = MaterialTheme.typography.bodySmall.copy(color = if (isMuted) NeonRose else TextSecondary, fontSize = 11.sp)
              )
            }

            Button(
              onClick = { viewModel.toggleMute() },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (isMuted) NeonRose.copy(alpha = 0.2f) else DarkSurfaceElevated
              ),
              border = androidx.compose.foundation.BorderStroke(1.dp, if (isMuted) NeonRose else DarkSurfaceBorder),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(
                imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                contentDescription = "Mute",
                tint = if (isMuted) NeonRose else NeonCyan,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (isMuted) "Unmute" else "Mute Video",
                color = if (isMuted) NeonRose else TextPrimary,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }
    }
  }
}
