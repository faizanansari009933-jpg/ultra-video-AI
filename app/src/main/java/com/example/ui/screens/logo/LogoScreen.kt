package com.example.ui.screens.logo

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Copyright
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.LogoConfig
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
fun LogoScreen(
  viewModel: UltraVideoViewModel,
  onBackClick: () -> Unit
) {
  val activeProject by viewModel.activeProject.collectAsState()
  val isPlaying by viewModel.isPlaying.collectAsState()
  val currentTime by viewModel.currentTimeSec.collectAsState()
  val logoConfig by viewModel.logoConfig.collectAsState()
  val duration = activeProject.durationSeconds

  // Photo Picker launcher (Zero permission required)
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    uri?.let {
      viewModel.setLogoImageUri(it.toString())
    }
  }

  val cornerPresets = listOf("Top-Left", "Top-Right", "Bottom-Left", "Bottom-Right", "Center")

  val generatorStyles = listOf(
    "Cyber Badge",
    "Studio Monogram",
    "Clean Watermark",
    "Cinematic Serif",
    "Glassmorphic Tag",
    "Minimalist Stamp"
  )

  val colorPalette = listOf(
    Pair("Cyber Cyan", 0xFF00E5FF),
    Pair("Neon Amber", 0xFFFFB300),
    Pair("Electric Violet", 0xFF8B5CF6),
    Pair("Rose Gold", 0xFFFF4081),
    Pair("Pure White", 0xFFFFFFFF),
    Pair("Emerald Glow", 0xFF00E676)
  )

  val backgroundStyles = listOf(
    "Frosted Capsule",
    "Transparent",
    "Neon Border",
    "Solid Slate"
  )

  val presetBrandBadges = listOf(
    Triple("Ultra AI", "OFFICIAL 4K", 0xFF00E5FF),
    Triple("Faizan Ansari", "CREATIVE DIRECTOR", 0xFFFFB300),
    Triple("Studio Cinema", "8K MASTER", 0xFF8B5CF6),
    Triple("Protected by AI", "ALL RIGHTS RESERVED", 0xFF00E676),
    Triple("Pro Video AI", "CERTIFIED ORIGINAL", 0xFFFF4081)
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DarkCanvas)
  ) {
    UltraTopAppBar(
      title = "Watermark Studio",
      onBackClick = onBackClick,
      actions = {
        Button(
          onClick = { viewModel.saveCurrentProject() },
          colors = ButtonDefaults.buttonColors(containerColor = NeonViolet.copy(alpha = 0.3f)),
          border = androidx.compose.foundation.BorderStroke(1.dp, NeonViolet),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.padding(end = 8.dp).testTag("save_watermark_button")
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
      contentPadding = PaddingValues(top = 10.dp, bottom = 44.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Interactive Video Preview with live Watermark Overlay
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
          logoConfig = logoConfig,
          isMuted = viewModel.isMuted.collectAsState().value,
          onToggleMute = { viewModel.toggleMute() }
        )
      }

      // Master Overlay & Persistence Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            // Enable Watermark
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NeonAmber.copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.WaterDrop, contentDescription = null, tint = NeonAmber, modifier = Modifier.size(20.dp))
                }
                Column {
                  Text("Watermark Overlay", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                  Text("Display corner branding or ownership mark", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp))
                }
              }

              Switch(
                checked = logoConfig.enabled,
                onCheckedChange = { viewModel.updateLogoConfig(logoConfig.copy(enabled = it)) },
                colors = SwitchDefaults.colors(checkedThumbColor = NeonAmber, checkedTrackColor = NeonViolet),
                modifier = Modifier.testTag("toggle_watermark_switch")
              )
            }

            Spacer(modifier = Modifier.height(14.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DarkSurfaceBorder))
            Spacer(modifier = Modifier.height(14.dp))

            // Persistent Overlay Toggle
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NeonCyan.copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    if (logoConfig.isPersistent) Icons.Default.Lock else Icons.Default.LockOpen,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                  )
                }
                Column {
                  Text("Persistent Overlay", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                  Text(
                    text = if (logoConfig.isPersistent) "Locks watermark across 100% of video duration" else "Custom timeline range enabled",
                    style = MaterialTheme.typography.bodySmall.copy(color = if (logoConfig.isPersistent) NeonCyan else TextSecondary, fontSize = 11.sp)
                  )
                }
              }

              Switch(
                checked = logoConfig.isPersistent,
                onCheckedChange = { viewModel.setLogoPersistent(it) },
                colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = NeonViolet),
                modifier = Modifier.testTag("toggle_persistent_switch")
              )
            }
          }
        }
      }

      // Source Mode Selector (Upload vs Generate)
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "WATERMARK CREATION MODE",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
              )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              val isUpload = logoConfig.sourceType == "UPLOAD"
              // Tab 1: Upload
              Surface(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .clickable { viewModel.setLogoSourceType("UPLOAD") }
                  .testTag("tab_upload_mode"),
                shape = RoundedCornerShape(12.dp),
                color = if (isUpload) NeonViolet.copy(alpha = 0.25f) else DarkSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isUpload) NeonViolet else DarkSurfaceBorder
                )
              ) {
                Row(
                  modifier = Modifier.padding(vertical = 12.dp),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    Icons.Default.FileUpload,
                    contentDescription = null,
                    tint = if (isUpload) NeonCyan else TextSecondary,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Upload Image",
                    color = if (isUpload) Color.White else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                  )
                }
              }

              // Tab 2: Generate
              val isGenerated = logoConfig.sourceType == "GENERATED"
              Surface(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .clickable { viewModel.setLogoSourceType("GENERATED") }
                  .testTag("tab_generate_mode"),
                shape = RoundedCornerShape(12.dp),
                color = if (isGenerated) NeonViolet.copy(alpha = 0.25f) else DarkSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isGenerated) NeonViolet else DarkSurfaceBorder
                )
              ) {
                Row(
                  modifier = Modifier.padding(vertical = 12.dp),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = if (isGenerated) NeonAmber else TextSecondary,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Generate Text Logo",
                    color = if (isGenerated) Color.White else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                  )
                }
              }
            }
          }
        }
      }

      // Upload Mode Controls
      if (logoConfig.sourceType == "UPLOAD") {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = "UPLOAD CUSTOM WATERMARK IMAGE",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = TextSecondary,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp
                )
              )

              Spacer(modifier = Modifier.height(12.dp))

              // If image selected
              if (!logoConfig.imageUri.isNullOrEmpty()) {
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = DarkSurfaceElevated,
                  border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.4f)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                      AsyncImage(
                        model = logoConfig.imageUri,
                        contentDescription = "Selected Watermark",
                        modifier = Modifier
                          .size(48.dp)
                          .clip(RoundedCornerShape(8.dp))
                          .background(Color.Black)
                      )
                      Column {
                        Text(
                          text = "Custom Watermark Image Loaded",
                          color = TextPrimary,
                          fontWeight = FontWeight.Bold,
                          fontSize = 13.sp
                        )
                        Text(
                          text = "Applying as corner overlay",
                          color = NeonCyan,
                          fontSize = 11.sp
                        )
                      }
                    }

                    IconButton(
                      onClick = { viewModel.setLogoImageUri(null) },
                      modifier = Modifier.testTag("clear_uploaded_image_button")
                    ) {
                      Icon(Icons.Default.Clear, contentDescription = "Clear", tint = NeonRose)
                    }
                  }
                }

                Spacer(modifier = Modifier.height(10.dp))
              }

              // Upload Button
              Button(
                onClick = {
                  photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                  )
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("pick_logo_image_button"),
                colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                shape = RoundedCornerShape(12.dp)
              ) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (logoConfig.imageUri.isNullOrEmpty()) "Choose Image from Device (PNG/JPG)" else "Replace Image with New File",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              }

              Spacer(modifier = Modifier.height(16.dp))
              Text(
                text = "OR CHOOSE A STUDIO PRESET EMBLEM",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = TextSecondary,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp
                )
              )
              Spacer(modifier = Modifier.height(8.dp))

              // Preset Badges
              LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(presetBrandBadges) { (title, tag, colorHex) ->
                  Surface(
                    modifier = Modifier
                      .clip(RoundedCornerShape(10.dp))
                      .clickable {
                        viewModel.updateLogoConfig(
                          logoConfig.copy(
                            sourceType = "GENERATED",
                            logoTitle = title,
                            tagline = tag,
                            colorThemeHex = colorHex,
                            imageUri = null
                          )
                        )
                      },
                    shape = RoundedCornerShape(10.dp),
                    color = DarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(colorHex).copy(alpha = 0.4f))
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                      Box(
                        modifier = Modifier
                          .size(10.dp)
                          .clip(CircleShape)
                          .background(Color(colorHex))
                      )
                      Column {
                        Text(title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text(tag, color = TextSecondary, fontSize = 9.sp)
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

      // Generate Mode Controls
      if (logoConfig.sourceType == "GENERATED") {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = "TEXT-BASED LOGO GENERATOR",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = TextSecondary,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp
                )
              )

              Spacer(modifier = Modifier.height(12.dp))

              // Brand Title Input
              Text("Brand Name / Watermark Text", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
              Spacer(modifier = Modifier.height(6.dp))
              OutlinedTextField(
                value = logoConfig.logoTitle,
                onValueChange = { viewModel.updateLogoConfig(logoConfig.copy(logoTitle = it)) },
                modifier = Modifier.fillMaxWidth().testTag("watermark_text_input"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = NeonCyan,
                  unfocusedBorderColor = DarkSurfaceBorder,
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                leadingIcon = {
                  Icon(Icons.Default.TextFields, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                }
              )

              Spacer(modifier = Modifier.height(12.dp))

              // Tagline Input
              Text("Tagline / Subtitle (Optional)", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
              Spacer(modifier = Modifier.height(6.dp))
              OutlinedTextField(
                value = logoConfig.tagline,
                onValueChange = { viewModel.updateLogoConfig(logoConfig.copy(tagline = it)) },
                modifier = Modifier.fillMaxWidth().testTag("watermark_tagline_input"),
                placeholder = { Text("e.g. OFFICIAL 4K, ALL RIGHTS RESERVED", color = TextSecondary, fontSize = 12.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = NeonAmber,
                  unfocusedBorderColor = DarkSurfaceBorder,
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
              )

              Spacer(modifier = Modifier.height(16.dp))

              // Generator Style Selector
              Text("Logo Design Style", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
              Spacer(modifier = Modifier.height(8.dp))
              LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(generatorStyles) { style ->
                  val isSel = logoConfig.generatorStyle == style
                  Surface(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .clickable { viewModel.updateLogoConfig(logoConfig.copy(generatorStyle = style)) }
                      .testTag("gen_style_$style"),
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSel) NeonAmber else DarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonAmber else DarkSurfaceBorder)
                  ) {
                    Text(
                      text = style,
                      modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isSel) Color.Black else TextPrimary,
                        fontWeight = FontWeight.Bold
                      )
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              // Color Palette Chips
              Text("Accent Color Palette", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
              Spacer(modifier = Modifier.height(8.dp))
              LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(colorPalette) { (name, hex) ->
                  val isSel = logoConfig.colorThemeHex == hex
                  Surface(
                    modifier = Modifier
                      .clip(RoundedCornerShape(20.dp))
                      .clickable { viewModel.updateLogoConfig(logoConfig.copy(colorThemeHex = hex)) }
                      .testTag("color_chip_$hex"),
                    shape = RoundedCornerShape(20.dp),
                    color = DarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(
                      if (isSel) 2.dp else 1.dp,
                      if (isSel) Color(hex) else DarkSurfaceBorder
                    )
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                      Box(
                        modifier = Modifier
                          .size(12.dp)
                          .clip(CircleShape)
                          .background(Color(hex))
                      )
                      Text(name, color = if (isSel) Color(hex) else TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                  }
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              // Background Shape
              Text("Container Framing", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
              Spacer(modifier = Modifier.height(8.dp))
              LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(backgroundStyles) { bg ->
                  val isSel = logoConfig.backgroundStyle == bg
                  Surface(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .clickable { viewModel.updateLogoConfig(logoConfig.copy(backgroundStyle = bg)) },
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSel) NeonCyan else DarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonCyan else DarkSurfaceBorder)
                  ) {
                    Text(
                      text = bg,
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
      }

      // Corner Placement & Margin Controls
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "CORNER PLACEMENT & EDGE OFFSET",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
              )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Corner Presets
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(cornerPresets) { preset ->
                val isSel = logoConfig.cornerPreset == preset
                Surface(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { viewModel.updateLogoConfig(logoConfig.copy(cornerPreset = preset)) }
                    .testTag("watermark_corner_$preset"),
                  shape = RoundedCornerShape(8.dp),
                  color = if (isSel) NeonAmber else DarkSurfaceElevated,
                  border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonAmber else DarkSurfaceBorder)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    if (isSel) Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                    Text(
                      text = preset,
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isSel) Color.Black else TextPrimary,
                        fontWeight = FontWeight.Bold
                      )
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Edge Margin Offset Slider
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Distance from Edge (Padding)", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
              Text(
                text = "${logoConfig.edgeMarginDp} dp",
                style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
              )
            }
            Slider(
              value = logoConfig.edgeMarginDp.toFloat(),
              onValueChange = { viewModel.updateLogoConfig(logoConfig.copy(edgeMarginDp = it.roundToInt())) },
              valueRange = 6f..40f,
              colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
            )
          }
        }
      }

      // Scale, Opacity & Rotation Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "SCALE, OPACITY & ROTATION",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
              )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Size / Scale
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Watermark Scale", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
              Text(
                text = "${(logoConfig.scale * 100).roundToInt()}%",
                style = MaterialTheme.typography.labelSmall.copy(color = NeonAmber, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
              )
            }
            Slider(
              value = logoConfig.scale,
              onValueChange = { viewModel.updateLogoConfig(logoConfig.copy(scale = it)) },
              valueRange = 0.4f..2.2f,
              colors = SliderDefaults.colors(thumbColor = NeonAmber, activeTrackColor = NeonAmber)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Opacity
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Opacity (Transparency)", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
              Text(
                text = "${(logoConfig.opacity * 100).roundToInt()}%",
                style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
              )
            }
            Slider(
              value = logoConfig.opacity,
              onValueChange = { viewModel.updateLogoConfig(logoConfig.copy(opacity = it)) },
              valueRange = 0.1f..1.0f,
              colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Rotation with Reset Button
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Rotation Angle", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
                if (logoConfig.rotation != 0f) {
                  Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = NeonRose.copy(alpha = 0.2f),
                    modifier = Modifier.clickable { viewModel.updateLogoConfig(logoConfig.copy(rotation = 0f)) }
                  ) {
                    Text("Reset 0°", color = NeonRose, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                  }
                }
              }
              Text(
                text = "${logoConfig.rotation.roundToInt()}°",
                style = MaterialTheme.typography.labelSmall.copy(color = NeonViolet, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
              )
            }
            Slider(
              value = logoConfig.rotation,
              onValueChange = { viewModel.updateLogoConfig(logoConfig.copy(rotation = it)) },
              valueRange = -180f..180f,
              colors = SliderDefaults.colors(thumbColor = NeonViolet, activeTrackColor = NeonViolet)
            )
          }
        }
      }

      // Non-persistent Timeline Range (only visible if persistent overlay is OFF)
      if (!logoConfig.isPersistent) {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonAmber.copy(alpha = 0.4f))
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Custom Appearance Window", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                Text(
                  text = "${logoConfig.startSec.toInt()}s — ${logoConfig.endSec.toInt()}s",
                  style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                )
              }
              Spacer(modifier = Modifier.height(8.dp))
              RangeSlider(
                value = logoConfig.startSec..logoConfig.endSec,
                onValueChange = {
                  viewModel.updateLogoConfig(logoConfig.copy(startSec = it.start, endSec = it.endInclusive))
                },
                valueRange = 0f..duration.toFloat(),
                colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonAmber)
              )
            }
          }
        }
      }

      // Save & Apply Button
      item {
        Spacer(modifier = Modifier.height(4.dp))
        Button(
          onClick = {
            viewModel.saveCurrentProject()
            onBackClick()
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("apply_watermark_button"),
          colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
          shape = RoundedCornerShape(14.dp)
        ) {
          Icon(Icons.Default.Check, contentDescription = null, tint = TextPrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Apply Watermark to Video",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = TextPrimary
          )
        }
      }
    }
  }
}
