package com.example.ui.screens.text

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.domain.model.TextLayer
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
fun TextScreen(
  viewModel: UltraVideoViewModel,
  onBackClick: () -> Unit
) {
  val activeProject by viewModel.activeProject.collectAsState()
  val isPlaying by viewModel.isPlaying.collectAsState()
  val currentTime by viewModel.currentTimeSec.collectAsState()
  val textLayers by viewModel.textLayers.collectAsState()
  val duration = activeProject.durationSeconds

  var selectedLayerId by remember(textLayers) {
    mutableStateOf(textLayers.firstOrNull()?.id ?: "")
  }

  val activeLayer = textLayers.firstOrNull { it.id == selectedLayerId } ?: textLayers.firstOrNull()

  val fonts = listOf("Futuristic", "Cyberpunk Neon", "Modern Sans", "Cinematic Serif", "Matrix Code")
  val animations = listOf("Neon Glow", "Fade In", "Pop In", "Typewriter", "Glitch Wave", "Slide Up")
  val colorPalette = listOf(
    0xFF00E5FF to NeonCyan,
    0xFF8B5CF6 to NeonViolet,
    0xFFFF0055 to NeonRose,
    0xFF00E676 to NeonEmerald,
    0xFFFFB300 to NeonAmber,
    0xFFFFFFFF to Color.White
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DarkCanvas)
  ) {
    UltraTopAppBar(
      title = "Add Text & Typography",
      onBackClick = onBackClick,
      actions = {
        Button(
          onClick = { viewModel.saveCurrentProject() },
          colors = ButtonDefaults.buttonColors(containerColor = NeonViolet.copy(alpha = 0.25f)),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.padding(end = 8.dp)
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
      // Interactive Video Preview showing live active Text layers
      item {
        VideoCanvasPreview(
          aspectRatioString = activeProject.aspectRatio,
          resolutionLabel = activeProject.resolution,
          durationSec = duration,
          currentPlayTimeSec = currentTime,
          onPlayTimeChange = { viewModel.setPlayTime(it) },
          isPlaying = isPlaying,
          onTogglePlay = { viewModel.togglePlay() },
          showSplitCompare = false,
          textLayers = textLayers,
          isMuted = viewModel.isMuted.collectAsState().value,
          onToggleMute = { viewModel.toggleMute() }
        )
      }

      // Layer Selector Strip & Add Layer Button
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "TEXT LAYERS (${textLayers.size})",
            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
          )

          Button(
            onClick = {
              viewModel.addTextLayer("NEW CINEMATIC TITLE", "Futuristic", 0xFF00E5FF)
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
            modifier = Modifier.testTag("add_new_text_layer_button")
          ) {
            Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add Layer", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      // Horizontal Layer Selector
      item {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(textLayers) { layer ->
            val isSel = layer.id == selectedLayerId
            Surface(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { selectedLayerId = layer.id },
              shape = RoundedCornerShape(8.dp),
              color = if (isSel) NeonViolet else DarkSurfaceElevated,
              border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonCyan else DarkSurfaceBorder)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = layer.text.take(16),
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = if (isSel) Color.White else TextPrimary,
                    fontWeight = FontWeight.SemiBold
                  )
                )

                if (textLayers.size > 1) {
                  IconButton(
                    onClick = { viewModel.removeTextLayer(layer.id) },
                    modifier = Modifier.size(20.dp)
                  ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
                  }
                }
              }
            }
          }
        }
      }

      if (activeLayer != null) {
        // Text Input Field
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text("Overlay Text Content", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
              Spacer(modifier = Modifier.height(8.dp))
              OutlinedTextField(
                value = activeLayer.text,
                onValueChange = { viewModel.updateTextLayer(activeLayer.copy(text = it)) },
                modifier = Modifier.fillMaxWidth().testTag("text_layer_input"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = NeonCyan,
                  unfocusedBorderColor = DarkSurfaceBorder,
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(10.dp)
              )
            }
          }
        }

        // Font Style Selector
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.FontDownload, contentDescription = "Font", tint = NeonCyan, modifier = Modifier.size(18.dp))
                Text("Font Family", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
              }
              Spacer(modifier = Modifier.height(10.dp))
              LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(fonts) { f ->
                  val isSel = activeLayer.fontName == f
                  Surface(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .clickable { viewModel.updateTextLayer(activeLayer.copy(fontName = f)) },
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSel) NeonCyan else DarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonCyan else DarkSurfaceBorder)
                  ) {
                    Text(
                      text = f,
                      modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
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

        // Color & Animation Selector
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text("Color Palette", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
              Spacer(modifier = Modifier.height(10.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                colorPalette.forEach { (cVal, cColor) ->
                  val isSel = activeLayer.color == cVal
                  Box(
                    modifier = Modifier
                      .size(36.dp)
                      .clip(CircleShape)
                      .background(cColor)
                      .border(if (isSel) 3.dp else 1.dp, if (isSel) Color.White else Color.Transparent, CircleShape)
                      .clickable { viewModel.updateTextLayer(activeLayer.copy(color = cVal)) }
                  )
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.Animation, contentDescription = "Animation", tint = NeonRose, modifier = Modifier.size(18.dp))
                Text("Text Motion & Animation", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
              }
              Spacer(modifier = Modifier.height(10.dp))
              LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(animations) { anim ->
                  val isSel = activeLayer.animation == anim
                  Surface(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .clickable { viewModel.updateTextLayer(activeLayer.copy(animation = anim)) },
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSel) NeonRose else DarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonRose else DarkSurfaceBorder)
                  ) {
                    Text(
                      text = anim,
                      modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
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

        // Size, Position & Opacity Sliders
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              // Font Size
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Font Size", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
                Text("${activeLayer.fontSize.roundToInt()}sp", style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontFamily = FontFamily.Monospace))
              }
              Slider(
                value = activeLayer.fontSize,
                onValueChange = { viewModel.updateTextLayer(activeLayer.copy(fontSize = it)) },
                valueRange = 14f..64f,
                colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
              )

              Spacer(modifier = Modifier.height(8.dp))

              // Vertical Position
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Vertical Position (Y)", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
                Text("${(activeLayer.posY * 100).roundToInt()}%", style = MaterialTheme.typography.labelSmall.copy(color = NeonViolet, fontFamily = FontFamily.Monospace))
              }
              Slider(
                value = activeLayer.posY,
                onValueChange = { viewModel.updateTextLayer(activeLayer.copy(posY = it)) },
                valueRange = 0.1f..0.95f,
                colors = SliderDefaults.colors(thumbColor = NeonViolet, activeTrackColor = NeonViolet)
              )

              Spacer(modifier = Modifier.height(8.dp))

              // Opacity
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Opacity", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
                Text("${(activeLayer.opacity * 100).roundToInt()}%", style = MaterialTheme.typography.labelSmall.copy(color = NeonAmber, fontFamily = FontFamily.Monospace))
              }
              Slider(
                value = activeLayer.opacity,
                onValueChange = { viewModel.updateTextLayer(activeLayer.copy(opacity = it)) },
                valueRange = 0.1f..1.0f,
                colors = SliderDefaults.colors(thumbColor = NeonAmber, activeTrackColor = NeonAmber)
              )
            }
          }
        }

        // Duration on Timeline
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
                Text("Layer Duration on Timeline", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                Text(
                  text = "${activeLayer.startSec.toInt()}s — ${activeLayer.endSec.toInt()}s",
                  style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontFamily = FontFamily.Monospace)
                )
              }
              Spacer(modifier = Modifier.height(6.dp))
              RangeSlider(
                value = activeLayer.startSec..activeLayer.endSec,
                onValueChange = {
                  viewModel.updateTextLayer(activeLayer.copy(startSec = it.start, endSec = it.endInclusive))
                },
                valueRange = 0f..duration.toFloat(),
                colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonViolet)
              )
            }
          }
        }
      }
    }
  }
}
