package com.example.ui.screens.enhance

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.domain.model.ScreenRoute
import com.example.ui.components.UltraTopAppBar
import com.example.ui.components.VideoCanvasPreview
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
fun EnhanceScreen(
  viewModel: UltraVideoViewModel,
  onBackClick: () -> Unit
) {
  val activeProject by viewModel.activeProject.collectAsState()
  val targetRes by viewModel.targetResolution.collectAsState()
  val denoiseLevel by viewModel.denoiseLevel.collectAsState()
  val deblurEnabled by viewModel.deblurEnabled.collectAsState()
  val sharpenLevel by viewModel.sharpenLevel.collectAsState()
  val superResScale by viewModel.superResScale.collectAsState()
  val faceRestoration by viewModel.faceRestorationEnabled.collectAsState()
  val detailEnhance by viewModel.detailEnhanceLevel.collectAsState()
  val colorImprovement by viewModel.colorImprovement.collectAsState()
  val lightingImprovement by viewModel.lightingImprovement.collectAsState()
  val identityPreservation by viewModel.identityPreservation.collectAsState()

  val isPlaying by viewModel.isPlaying.collectAsState()
  val currentTime by viewModel.currentTimeSec.collectAsState()
  val splitPos by viewModel.splitComparePosition.collectAsState()

  val isEnhancing by viewModel.isEnhancing.collectAsState()
  val enhanceProgress by viewModel.enhanceProgress.collectAsState()
  val enhanceStage by viewModel.enhanceStage.collectAsState()

  var compareActive by remember { mutableStateOf(true) }

  val resolutions = listOf("720p", "1080p Full HD", "2K", "4K Ultra HD", "8K Ultra HD")

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DarkCanvas)
  ) {
    UltraTopAppBar(
      title = "AI Video Enhance",
      onBackClick = onBackClick,
      actions = {
        Button(
          onClick = { viewModel.saveCurrentProject() },
          colors = ButtonDefaults.buttonColors(containerColor = NeonViolet.copy(alpha = 0.25f)),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .padding(end = 8.dp)
            .testTag("save_enhance_button")
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
      // Interactive Video Preview with Before/After Split Comparison
      item {
        VideoCanvasPreview(
          aspectRatioString = activeProject.aspectRatio,
          resolutionLabel = targetRes,
          durationSec = activeProject.durationSeconds,
          currentPlayTimeSec = currentTime,
          onPlayTimeChange = { viewModel.setPlayTime(it) },
          isPlaying = isPlaying,
          onTogglePlay = { viewModel.togglePlay() },
          showSplitCompare = compareActive,
          splitPosition = splitPos,
          onSplitPositionChange = { viewModel.setSplitComparePosition(it) },
          isMuted = viewModel.isMuted.collectAsState().value,
          onToggleMute = { viewModel.toggleMute() }
        )
      }

      // Compare View Toggle Bar
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.Compare, contentDescription = "Compare", tint = NeonCyan, modifier = Modifier.size(18.dp))
            Text(
              text = "Split Before / After Slider",
              style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold)
            )
          }

          Switch(
            checked = compareActive,
            onCheckedChange = { compareActive = it },
            colors = SwitchDefaults.colors(
              checkedThumbColor = NeonCyan,
              checkedTrackColor = NeonViolet,
              uncheckedTrackColor = DarkSurfaceBorder
            )
          )
        }
      }

      // Live AI Processing Progress Card
      item {
        AnimatedVisibility(visible = isEnhancing) {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonViolet)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  Icon(Icons.Default.AutoAwesome, contentDescription = "Processing", tint = NeonAmber, modifier = Modifier.size(18.dp))
                  Text("AI Inference Processing...", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Text(
                  text = "${(enhanceProgress * 100).roundToInt()}%",
                  color = NeonCyan,
                  fontWeight = FontWeight.ExtraBold,
                  fontFamily = FontFamily.Monospace
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              LinearProgressIndicator(
                progress = { enhanceProgress },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(6.dp)
                  .clip(RoundedCornerShape(3.dp)),
                color = NeonViolet,
                trackColor = DarkSurfaceBorder
              )

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = enhanceStage,
                style = MaterialTheme.typography.bodySmall.copy(
                  color = TextSecondary,
                  fontSize = 11.sp,
                  fontFamily = FontFamily.Monospace
                )
              )
            }
          }
        }
      }

      // Resolution Selection Strip
      item {
        Text(
          text = "TARGET RESOLUTION",
          style = MaterialTheme.typography.labelSmall.copy(
            color = TextSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(resolutions) { res ->
            val selected = targetRes == res
            Surface(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable { viewModel.setTargetResolution(res) }
                .testTag("res_option_$res"),
              shape = RoundedCornerShape(10.dp),
              color = if (selected) NeonViolet else DarkSurfaceElevated,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (selected) NeonCyan else DarkSurfaceBorder
              )
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                if (selected) {
                  Icon(Icons.Default.Check, contentDescription = "Selected", tint = Color.White, modifier = Modifier.size(14.dp))
                }
                Text(
                  text = res,
                  style = MaterialTheme.typography.labelMedium.copy(
                    color = if (selected) Color.White else TextPrimary,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                  )
                )
              }
            }
          }
        }
      }

      // AI Features Grid & Sliders
      item {
        Text(
          text = "AI NEURAL ENHANCEMENT CONTROLS",
          style = MaterialTheme.typography.labelSmall.copy(
            color = TextSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        )
      }

      // 1. AI Denoise
      item {
        EnhanceSliderCard(
          title = "AI Denoise",
          subtitle = "Neural noise reduction while keeping fine grain",
          value = denoiseLevel,
          onValueChange = { viewModel.setDenoiseLevel(it) },
          accentColor = NeonViolet
        )
      }

      // 2. Deblur & Sharpening
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
              Column {
                Text("AI Motion & Optical Deblur", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                Text("Correct camera shakes and focal blur", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp))
              }
              Switch(
                checked = deblurEnabled,
                onCheckedChange = { viewModel.toggleDeblur() },
                colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = NeonViolet)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            EnhanceSliderRow(
              label = "Sharpening Level",
              value = sharpenLevel,
              onValueChange = { viewModel.setSharpenLevel(it) },
              accentColor = NeonCyan
            )
          }
        }
      }

      // 3. Super-Resolution & Detail Enhancement
      item {
        EnhanceSliderCard(
          title = "Detail Enhancement & Micro-Textures",
          subtitle = "Recovers skin pores, fabrics, and HDR dynamic range",
          value = detailEnhance,
          onValueChange = { viewModel.setDetailEnhanceLevel(it) },
          accentColor = NeonAmber
        )
      }

      // 4. Natural Face Restoration & Identity Preservation
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
              Column {
                Text("Natural Face Restoration", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                Text("GFPGAN & CodeFormer biometric neural enhancement", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp))
              }
              Switch(
                checked = faceRestoration,
                onCheckedChange = { viewModel.toggleFaceRestoration() },
                colors = SwitchDefaults.colors(checkedThumbColor = NeonEmerald, checkedTrackColor = NeonViolet)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            EnhanceSliderRow(
              label = "Original Facial Identity Preservation",
              value = identityPreservation,
              onValueChange = { viewModel.setIdentityPreservation(it) },
              accentColor = NeonEmerald
            )
          }
        }
      }

      // 5. Color & Lighting Improvement
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            EnhanceSliderRow(
              label = "Color Improvement (AI Color Grade & Vibrance)",
              value = colorImprovement,
              onValueChange = { viewModel.setColorImprovement(it) },
              accentColor = Color(0xFFFF5252)
            )

            Spacer(modifier = Modifier.height(14.dp))

            EnhanceSliderRow(
              label = "Lighting Improvement (Low-Light & Shadow Recovery)",
              value = lightingImprovement,
              onValueChange = { viewModel.setLightingImprovement(it) },
              accentColor = NeonAmber
            )
          }
        }
      }

      // Process Action Button
      item {
        Spacer(modifier = Modifier.height(8.dp))
        Button(
          onClick = { viewModel.startAiEnhancementProcess() },
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .testTag("start_enhance_process_button"),
          enabled = !isEnhancing,
          colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
          shape = RoundedCornerShape(14.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Icon(Icons.Default.AutoAwesome, contentDescription = "Run AI", tint = NeonCyan)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isEnhancing) "Processing Video..." else "Start AI $targetRes Enhancement",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
            )
          }
        }
      }
    }
  }
}

@Composable
fun EnhanceSliderCard(
  title: String,
  subtitle: String,
  value: Float,
  onValueChange: (Float) -> Unit,
  accentColor: Color
) {
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
        Column {
          Text(title, style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
          Text(subtitle, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp))
        }
        Text(
          text = "${(value * 100).roundToInt()}%",
          style = MaterialTheme.typography.labelMedium.copy(color = accentColor, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        )
      }

      Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = 0f..1f,
        colors = SliderDefaults.colors(
          thumbColor = accentColor,
          activeTrackColor = accentColor,
          inactiveTrackColor = DarkSurfaceBorder
        )
      )
    }
  }
}

@Composable
fun EnhanceSliderRow(
  label: String,
  value: Float,
  onValueChange: (Float) -> Unit,
  accentColor: Color
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(label, style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
    Text(
      text = "${(value * 100).roundToInt()}%",
      style = MaterialTheme.typography.labelSmall.copy(color = accentColor, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    )
  }
  Slider(
    value = value,
    onValueChange = onValueChange,
    valueRange = 0f..1f,
    colors = SliderDefaults.colors(
      thumbColor = accentColor,
      activeTrackColor = accentColor,
      inactiveTrackColor = DarkSurfaceBorder
    )
  )
}
