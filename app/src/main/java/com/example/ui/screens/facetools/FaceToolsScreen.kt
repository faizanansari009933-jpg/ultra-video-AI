package com.example.ui.screens.facetools

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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
fun FaceToolsScreen(
  viewModel: UltraVideoViewModel,
  onBackClick: () -> Unit
) {
  val activeProject by viewModel.activeProject.collectAsState()
  val isPlaying by viewModel.isPlaying.collectAsState()
  val currentTime by viewModel.currentTimeSec.collectAsState()

  val faceDetectionActive by viewModel.faceDetectionActive.collectAsState()
  val faceTrackingActive by viewModel.faceTrackingActive.collectAsState()
  val naturalFaceRestoration by viewModel.naturalFaceRestorationActive.collectAsState()
  val detectedFaces by viewModel.detectedFaces.collectAsState()

  val consentGranted by viewModel.faceSwapConsentGranted.collectAsState()
  val showConsentDialog by viewModel.showFaceSwapConsentDialog.collectAsState()
  val selectedSwapFace by viewModel.selectedSwapSourceFace.collectAsState()

  var faceRetouchLevel by remember { mutableFloatStateOf(0.85f) }
  var swapBlendWeight by remember { mutableFloatStateOf(0.75f) }

  // Consent checkbox states for the dialog
  var consentCheck1 by remember { mutableStateOf(false) }
  var consentCheck2 by remember { mutableStateOf(false) }

  val sourceFaces = listOf(
    "Studio Model 01",
    "Studio Model 02",
    "Digital Avatar Alpha",
    "Custom Upload Face"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DarkCanvas)
  ) {
    UltraTopAppBar(
      title = "Face Tools",
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
      // Interactive Video Preview with Face Bounding Boxes & Landmarks
      item {
        VideoCanvasPreview(
          aspectRatioString = activeProject.aspectRatio,
          resolutionLabel = activeProject.resolution,
          durationSec = activeProject.durationSeconds,
          currentPlayTimeSec = currentTime,
          onPlayTimeChange = { viewModel.setPlayTime(it) },
          isPlaying = isPlaying,
          onTogglePlay = { viewModel.togglePlay() },
          showSplitCompare = false,
          showFaceTracking = faceTrackingActive,
          detectedFaces = detectedFaces,
          isMuted = viewModel.isMuted.collectAsState().value,
          onToggleMute = { viewModel.toggleMute() }
        )
      }

      // Live Detection Stats Banner
      item {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = DarkSurfaceElevated,
          border = androidx.compose.foundation.BorderStroke(1.dp, NeonEmerald.copy(alpha = 0.4f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(NeonEmerald))
              Text("AI Face Engine: Active", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Text(
              text = "${detectedFaces.size} Faces Tracked • 98% Conf",
              color = NeonCyan,
              fontFamily = FontFamily.Monospace,
              fontSize = 11.sp
            )
          }
        }
      }

      // Feature 1: AI Face Enhance & Retouch
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
                Text("AI Face Enhance & Retouch", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                Text("Skin texture smoothing, eye clarity & dental brightness", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp))
              }
              Text(
                text = "${(faceRetouchLevel * 100).roundToInt()}%",
                style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
              )
            }

            Slider(
              value = faceRetouchLevel,
              onValueChange = { faceRetouchLevel = it },
              valueRange = 0f..1f,
              colors = SliderDefaults.colors(
                thumbColor = NeonCyan,
                activeTrackColor = NeonCyan,
                inactiveTrackColor = DarkSurfaceBorder
              )
            )
          }
        }
      }

      // Feature 2: Face Detection & Tracking Toggles
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text("Face Detection & Mesh Analysis", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                Text("68-point facial landmark geometry mapping", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp))
              }
              Switch(
                checked = faceDetectionActive,
                onCheckedChange = { viewModel.toggleFaceDetection() },
                colors = SwitchDefaults.colors(checkedThumbColor = NeonEmerald, checkedTrackColor = NeonViolet)
              )
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text("Face Tracking Across Frames", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                Text("Lock onto subjects dynamically through head rotation", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp))
              }
              Switch(
                checked = faceTrackingActive,
                onCheckedChange = { viewModel.toggleFaceTracking() },
                colors = SwitchDefaults.colors(checkedThumbColor = NeonEmerald, checkedTrackColor = NeonViolet)
              )
            }
          }
        }
      }

      // Feature 3: Natural Face Restoration
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
            Column(modifier = Modifier.weight(1f)) {
              Text("Natural Face Restoration", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
              Text(
                "Restores low-resolution, blurred portraits without plastic smoothing effect. Maintains natural identity.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Switch(
              checked = naturalFaceRestoration,
              onCheckedChange = { viewModel.toggleNaturalFaceRestoration() },
              colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = NeonViolet)
            )
          }
        }
      }

      // Feature 4: Consent-Based Face Swap
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (consentGranted) NeonEmerald else NeonAmber
          )
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                  imageVector = if (consentGranted) Icons.Default.GppGood else Icons.Default.Security,
                  contentDescription = "Consent",
                  tint = if (consentGranted) NeonEmerald else NeonAmber,
                  modifier = Modifier.size(22.dp)
                )
                Text(
                  text = "Consent-Based Face Swap",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                  )
                )
              }

              Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (consentGranted) NeonEmerald.copy(alpha = 0.2f) else NeonAmber.copy(alpha = 0.2f)
              ) {
                Text(
                  text = if (consentGranted) "CONSENT VERIFIED" else "CONSENT REQUIRED",
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = if (consentGranted) NeonEmerald else NeonAmber,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                  )
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "Ethical AI Policy: Face Swap requires explicit confirmation of consent from the face owner. Must not be used for impersonation, fraud, or non-consensual content.",
              style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 16.sp
              )
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (!consentGranted) {
              Button(
                onClick = { viewModel.requestFaceSwapConsent() },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("verify_face_swap_consent_button"),
                colors = ButtonDefaults.buttonColors(containerColor = NeonAmber),
                shape = RoundedCornerShape(10.dp)
              ) {
                Text("Verify Consent & Unlock Face Swap", color = Color.Black, fontWeight = FontWeight.Bold)
              }
            } else {
              // Source Face Options
              Text(
                text = "SELECT SOURCE FACE",
                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
              )
              Spacer(modifier = Modifier.height(8.dp))

              LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(sourceFaces) { face ->
                  val isSel = selectedSwapFace == face
                  Surface(
                    modifier = Modifier
                      .clip(RoundedCornerShape(10.dp))
                      .clickable { viewModel.selectSwapFace(face) },
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSel) NeonViolet else DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonCyan else DarkSurfaceBorder)
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                      Icon(Icons.Default.Person, contentDescription = face, tint = if (isSel) Color.White else NeonCyan, modifier = Modifier.size(16.dp))
                      Text(
                        text = face,
                        style = MaterialTheme.typography.bodySmall.copy(
                          color = if (isSel) Color.White else TextPrimary,
                          fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium
                        )
                      )
                    }
                  }
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Swap Blend Strength", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
                Text("${(swapBlendWeight * 100).roundToInt()}%", style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontFamily = FontFamily.Monospace))
              }

              Slider(
                value = swapBlendWeight,
                onValueChange = { swapBlendWeight = it },
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(thumbColor = NeonViolet, activeTrackColor = NeonViolet)
              )
            }
          }
        }
      }
    }

    // Consent Verification Dialog
    if (showConsentDialog) {
      AlertDialog(
        onDismissRequest = { viewModel.declineFaceSwapConsent() },
        containerColor = DarkSurfaceElevated,
        shape = RoundedCornerShape(20.dp),
        icon = {
          Icon(Icons.Default.Security, contentDescription = "Consent Alert", tint = NeonAmber, modifier = Modifier.size(32.dp))
        },
        title = {
          Text(
            text = "Ethical AI & Consent Verification",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
          )
        },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
              text = "Face Swap must require the user to confirm they have permission to use the source face and must not be designed for impersonation, fraud, harassment, or non-consensual content.",
              style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 13.sp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
              Checkbox(
                checked = consentCheck1,
                onCheckedChange = { consentCheck1 = it },
                colors = CheckboxDefaults.colors(checkedColor = NeonEmerald, checkmarkColor = Color.Black)
              )
              Text(
                text = "I confirm I have explicit permission from the face owner.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontSize = 12.sp)
              )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              Checkbox(
                checked = consentCheck2,
                onCheckedChange = { consentCheck2 = it },
                colors = CheckboxDefaults.colors(checkedColor = NeonEmerald, checkmarkColor = Color.Black)
              )
              Text(
                text = "I will NOT use this for impersonation, fraud, harassment, or non-consensual content.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontSize = 12.sp)
              )
            }
          }
        },
        confirmButton = {
          Button(
            onClick = {
              if (consentCheck1 && consentCheck2) {
                viewModel.grantFaceSwapConsent()
              }
            },
            enabled = consentCheck1 && consentCheck2,
            colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
            modifier = Modifier.testTag("confirm_face_swap_consent_button")
          ) {
            Text("I Confirm & Agree", color = Color.Black, fontWeight = FontWeight.Bold)
          }
        },
        dismissButton = {
          TextButton(onClick = { viewModel.declineFaceSwapConsent() }) {
            Text("Cancel", color = TextSecondary)
          }
        }
      )
    }
  }
}
