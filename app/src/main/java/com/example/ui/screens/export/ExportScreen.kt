package com.example.ui.screens.export

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.UltraTopAppBar
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.UltraVideoViewModel
import kotlin.math.roundToInt

@Composable
fun ExportScreen(
  viewModel: UltraVideoViewModel,
  onBackClick: () -> Unit
) {
  val activeProject by viewModel.activeProject.collectAsState()
  val exportSettings by viewModel.exportSettings.collectAsState()

  val isExporting by viewModel.isExporting.collectAsState()
  val exportProgress by viewModel.exportProgress.collectAsState()
  val exportFrames by viewModel.exportFrameCount.collectAsState()
  val exportCompleted by viewModel.exportCompleted.collectAsState()

  val resolutions = listOf("720p", "1080p", "2K", "4K Ultra HD", "8K Ultra HD")
  val fpsList = listOf(24, 30, 60, 120)
  val bitrates = listOf("Low (8 Mbps)", "Medium (18 Mbps)", "High (35 Mbps)", "Ultra (75 Mbps)")
  val formats = listOf("MP4 (H.264)", "MP4 (H.265 HEVC)", "MOV (Apple ProRes)", "MKV Master")

  val estimatedSizeMb = when (exportSettings.resolution) {
    "8K Ultra HD" -> 450
    "4K Ultra HD" -> 210
    "2K" -> 95
    "1080p" -> 45
    else -> 20
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DarkCanvas)
  ) {
    UltraTopAppBar(
      title = "Export Video",
      onBackClick = onBackClick
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Export Summary Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, NeonViolet.copy(alpha = 0.5f))
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = activeProject.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
              )
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = NeonCyan.copy(alpha = 0.18f),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.4f))
              ) {
                Text(
                  text = "EST. ${estimatedSizeMb} MB",
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                  style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "Duration: ${activeProject.durationSeconds}s • Aspect: ${activeProject.aspectRatio} • Speed: ${activeProject.videoSpeed}x",
              style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )
          }
        }
      }

      // Live Render Progress Bar if exporting
      item {
        AnimatedVisibility(visible = isExporting) {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  Icon(Icons.Default.Speed, contentDescription = "Rendering", tint = NeonAmber, modifier = Modifier.size(20.dp))
                  Text("Encoding Video Frames...", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Text(
                  text = "${(exportProgress * 100).roundToInt()}%",
                  color = NeonCyan,
                  fontWeight = FontWeight.ExtraBold,
                  fontFamily = FontFamily.Monospace
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              LinearProgressIndicator(
                progress = { exportProgress },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(8.dp)
                  .clip(RoundedCornerShape(4.dp)),
                color = NeonCyan,
                trackColor = DarkSurfaceBorder
              )

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "Rendered: $exportFrames frames",
                  style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontFamily = FontFamily.Monospace)
                )
                Text(
                  text = "ETA: ${((1f - exportProgress) * 6).roundToInt()}s",
                  style = MaterialTheme.typography.labelSmall.copy(color = NeonAmber, fontFamily = FontFamily.Monospace)
                )
              }
            }
          }
        }
      }

      // Resolution Selection
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text("EXPORT RESOLUTION", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontWeight = FontWeight.Bold, letterSpacing = 1.sp))
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(resolutions) { res ->
                val isSel = exportSettings.resolution == res
                Surface(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { viewModel.updateExportSettings(exportSettings.copy(resolution = res)) }
                    .testTag("export_res_$res"),
                  shape = RoundedCornerShape(8.dp),
                  color = if (isSel) NeonViolet else DarkSurfaceElevated,
                  border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonCyan else DarkSurfaceBorder)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    if (isSel) Icon(Icons.Default.Check, contentDescription = "Selected", tint = Color.White, modifier = Modifier.size(14.dp))
                    Text(
                      text = res,
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isSel) Color.White else TextPrimary,
                        fontWeight = FontWeight.Bold
                      )
                    )
                  }
                }
              }
            }
          }
        }
      }

      // Frame Rate (FPS)
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text("FRAME RATE (FPS)", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontWeight = FontWeight.Bold, letterSpacing = 1.sp))
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(fpsList) { fps ->
                val isSel = exportSettings.fps == fps
                Surface(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { viewModel.updateExportSettings(exportSettings.copy(fps = fps)) },
                  shape = RoundedCornerShape(8.dp),
                  color = if (isSel) NeonCyan else DarkSurfaceElevated,
                  border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonCyan else DarkSurfaceBorder)
                ) {
                  Text(
                    text = "${fps} FPS",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = if (isSel) Color.Black else TextPrimary,
                      fontWeight = FontWeight.Bold
                    )
                  )
                }
              }
            }
          }
        }
      }

      // Bitrate Selection
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text("ENCODING BITRATE QUALITY", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontWeight = FontWeight.Bold, letterSpacing = 1.sp))
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(bitrates) { b ->
                val isSel = exportSettings.bitrate == b
                Surface(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { viewModel.updateExportSettings(exportSettings.copy(bitrate = b)) },
                  shape = RoundedCornerShape(8.dp),
                  color = if (isSel) NeonAmber else DarkSurfaceElevated,
                  border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonAmber else DarkSurfaceBorder)
                ) {
                  Text(
                    text = b,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = if (isSel) Color.Black else TextPrimary,
                      fontWeight = FontWeight.Bold
                    )
                  )
                }
              }
            }
          }
        }
      }

      // Video Format / Codec
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text("VIDEO FORMAT & CODEC", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontWeight = FontWeight.Bold, letterSpacing = 1.sp))
            Spacer(modifier = Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              formats.forEach { fmt ->
                val isSel = exportSettings.format == fmt
                Surface(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { viewModel.updateExportSettings(exportSettings.copy(format = fmt)) },
                  shape = RoundedCornerShape(8.dp),
                  color = if (isSel) NeonViolet.copy(alpha = 0.25f) else DarkSurfaceElevated,
                  border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonCyan else DarkSurfaceBorder)
                ) {
                  Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = fmt,
                      style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isSel) NeonCyan else TextPrimary,
                        fontWeight = FontWeight.Bold
                      )
                    )
                    if (isSel) {
                      Icon(Icons.Default.CheckCircle, contentDescription = "Active", tint = NeonCyan, modifier = Modifier.size(16.dp))
                    }
                  }
                }
              }
            }
          }
        }
      }

      // Audio Preservation Switch
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Preserve Source Audio Track", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
              Text("Include stereo AAC audio channel in output file", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp))
            }
            Switch(
              checked = exportSettings.audioPreserved,
              onCheckedChange = { viewModel.updateExportSettings(exportSettings.copy(audioPreserved = it)) },
              colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = NeonViolet)
            )
          }
        }
      }

      // Export Button
      item {
        Spacer(modifier = Modifier.height(6.dp))
        Button(
          onClick = { viewModel.startExport() },
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .testTag("start_export_button"),
          enabled = !isExporting,
          colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
          shape = RoundedCornerShape(14.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Icon(Icons.Default.Upload, contentDescription = "Export", tint = NeonCyan)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isExporting) "Rendering..." else "Export Master Video (${exportSettings.resolution})",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
            )
          }
        }
      }
    }

    // Export Finished Dialog
    if (exportCompleted) {
      AlertDialog(
        onDismissRequest = { viewModel.resetExportState() },
        containerColor = DarkSurfaceElevated,
        shape = RoundedCornerShape(20.dp),
        icon = {
          Box(
            modifier = Modifier.size(56.dp).clip(CircleShape).background(NeonEmerald.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.DownloadDone, contentDescription = "Done", tint = NeonEmerald, modifier = Modifier.size(32.dp))
          }
        },
        title = {
          Text("Export Successful!", fontWeight = FontWeight.Bold, color = TextPrimary)
        },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
              "Your video has been rendered and saved to your project library.",
              color = TextSecondary,
              fontSize = 14.sp
            )
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = DarkSurface,
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text("Format: ${exportSettings.format}", color = TextPrimary, fontSize = 12.sp)
                Text("Resolution: ${exportSettings.resolution} @ ${exportSettings.fps} FPS", color = TextPrimary, fontSize = 12.sp)
                Text("Bitrate: ${exportSettings.bitrate}", color = TextPrimary, fontSize = 12.sp)
              }
            }
          }
        },
        confirmButton = {
          Button(
            onClick = {
              viewModel.resetExportState()
              onBackClick()
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald)
          ) {
            Text("Done", color = Color.Black, fontWeight = FontWeight.Bold)
          }
        }
      )
    }
  }
}
