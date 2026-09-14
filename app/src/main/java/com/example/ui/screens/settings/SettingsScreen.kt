package com.example.ui.screens.settings

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.UltraTopAppBar
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRose
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.UltraVideoViewModel

@Composable
fun SettingsScreen(
  viewModel: UltraVideoViewModel,
  onBackClick: () -> Unit
) {
  val themeSetting by viewModel.appThemeSetting.collectAsState()
  val languageSetting by viewModel.appLanguage.collectAsState()
  val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()

  var showLanguageDialog by remember { mutableStateOf(false) }
  var showThemeDialog by remember { mutableStateOf(false) }
  var showPrivacyDialog by remember { mutableStateOf(false) }

  val availableLanguages = listOf("English", "Spanish", "French", "German", "Hindi", "Japanese")
  val availableThemes = listOf("Ultra Dark (Cyberpunk)", "OLED Pure Black", "Studio Slate", "Deep Obsidian")

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DarkCanvas)
  ) {
    UltraTopAppBar(
      title = "Settings",
      onBackClick = onBackClick
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Preferences Section
      item {
        Text(
          text = "PREFERENCES & ENVIRONMENT",
          style = MaterialTheme.typography.labelSmall.copy(
            color = TextSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        )
      }

      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column {
            // Theme setting
            SettingsRowItem(
              icon = Icons.Default.ColorLens,
              iconTint = NeonViolet,
              title = "Theme Appearance",
              subtitle = themeSetting,
              onClick = { showThemeDialog = true }
            )

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DarkSurfaceBorder))

            // Language setting
            SettingsRowItem(
              icon = Icons.Default.Language,
              iconTint = NeonCyan,
              title = "Display Language",
              subtitle = languageSetting,
              onClick = { showLanguageDialog = true }
            )

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DarkSurfaceBorder))

            // Notifications
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF4285F4).copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color(0xFF4285F4), modifier = Modifier.size(20.dp))
                }
                Column {
                  Text("Render Completion Alerts", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
                  Text("Notify when 8K export finishes", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp))
                }
              }

              Switch(
                checked = notificationsEnabled,
                onCheckedChange = { viewModel.toggleNotifications() },
                colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = NeonViolet)
              )
            }
          }
        }
      }

      // Privacy & Security Section
      item {
        Text(
          text = "PRIVACY & SECURITY",
          style = MaterialTheme.typography.labelSmall.copy(
            color = TextSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        )
      }

      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column {
            SettingsRowItem(
              icon = Icons.Default.Security,
              iconTint = Color(0xFF00E676),
              title = "Local Neural Privacy Policy",
              subtitle = "On-device SQLite storage & zero cloud leakage",
              onClick = { showPrivacyDialog = true }
            )

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DarkSurfaceBorder))

            SettingsRowItem(
              icon = Icons.Default.Policy,
              iconTint = Color(0xFFFFB300),
              title = "Ethical AI Use & Terms of Service",
              subtitle = "Anti-impersonation & consent agreement policy",
              onClick = { showPrivacyDialog = true }
            )
          }
        }
      }

      // About Section
      item {
        Text(
          text = "ABOUT ULTRA VIDEO AI",
          style = MaterialTheme.typography.labelSmall.copy(
            color = TextSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        )
      }

      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
          border = androidx.compose.foundation.BorderStroke(1.dp, NeonViolet.copy(alpha = 0.4f))
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(NeonViolet)
                .border(2.dp, NeonCyan, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.AutoAwesome, contentDescription = "Logo", tint = Color.White, modifier = Modifier.size(30.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = "Ultra Video AI",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
              )
            )

            Text(
              text = "AI Video Enhancer & Editor",
              style = MaterialTheme.typography.bodySmall.copy(
                color = NeonCyan,
                fontWeight = FontWeight.Medium
              )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "Version 1.0.0 (Release Build)",
              style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
              shape = RoundedCornerShape(10.dp),
              color = DarkSurface,
              border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
            ) {
              Text(
                text = "Made by Faizan Ansari",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleSmall.copy(
                  color = TextPrimary,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp
                )
              )
            }
          }
        }
      }

      // Logout
      item {
        Button(
          onClick = { viewModel.logout() },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("settings_logout_button"),
          colors = ButtonDefaults.buttonColors(containerColor = NeonRose.copy(alpha = 0.2f)),
          border = androidx.compose.foundation.BorderStroke(1.dp, NeonRose.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = NeonRose, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Log Out", color = NeonRose, fontWeight = FontWeight.Bold)
        }
      }
    }

    // Language Dialog
    if (showLanguageDialog) {
      AlertDialog(
        onDismissRequest = { showLanguageDialog = false },
        containerColor = DarkSurfaceElevated,
        shape = RoundedCornerShape(18.dp),
        title = { Text("Select Language", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            availableLanguages.forEach { lang ->
              Surface(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .clickable {
                    viewModel.setAppLanguage(lang)
                    showLanguageDialog = false
                  },
                color = if (languageSetting == lang) NeonCyan.copy(alpha = 0.2f) else DarkSurface
              ) {
                Text(
                  text = lang,
                  color = if (languageSetting == lang) NeonCyan else TextPrimary,
                  fontWeight = FontWeight.SemiBold,
                  modifier = Modifier.padding(12.dp)
                )
              }
            }
          }
        },
        confirmButton = {
          TextButton(onClick = { showLanguageDialog = false }) {
            Text("Close", color = TextSecondary)
          }
        }
      )
    }

    // Theme Dialog
    if (showThemeDialog) {
      AlertDialog(
        onDismissRequest = { showThemeDialog = false },
        containerColor = DarkSurfaceElevated,
        shape = RoundedCornerShape(18.dp),
        title = { Text("Select Color Theme", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            availableThemes.forEach { th ->
              Surface(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .clickable {
                    viewModel.setAppTheme(th)
                    showThemeDialog = false
                  },
                color = if (themeSetting == th) NeonViolet.copy(alpha = 0.2f) else DarkSurface
              ) {
                Text(
                  text = th,
                  color = if (themeSetting == th) NeonCyan else TextPrimary,
                  fontWeight = FontWeight.SemiBold,
                  modifier = Modifier.padding(12.dp)
                )
              }
            }
          }
        },
        confirmButton = {
          TextButton(onClick = { showThemeDialog = false }) {
            Text("Close", color = TextSecondary)
          }
        }
      )
    }

    // Privacy Dialog
    if (showPrivacyDialog) {
      AlertDialog(
        onDismissRequest = { showPrivacyDialog = false },
        containerColor = DarkSurfaceElevated,
        shape = RoundedCornerShape(18.dp),
        title = { Text("Privacy & Ethical Guidelines", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
              "• Local Storage: All projects, credentials, and video layers are stored locally in Room SQLite. No unauthorized video upload occurs.",
              color = TextSecondary,
              fontSize = 13.sp
            )
            Text(
              "• Biometrics: Facial detection and landmark data is computed securely on-device for enhancement purposes.",
              color = TextSecondary,
              fontSize = 13.sp
            )
            Text(
              "• Ethical AI: Impersonation, non-consensual face modifications, and harmful content creation are strictly prohibited under terms of service.",
              color = TextSecondary,
              fontSize = 13.sp
            )
          }
        },
        confirmButton = {
          Button(onClick = { showPrivacyDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)) {
            Text("I Understand", color = TextPrimary)
          }
        }
      )
    }
  }
}

@Composable
fun SettingsRowItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconTint: Color,
  title: String,
  subtitle: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(16.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(iconTint.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(20.dp))
      }

      Column {
        Text(title, style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
        Text(subtitle, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp))
      }
    }

    Icon(Icons.Default.ChevronRight, contentDescription = "Go", tint = TextSecondary.copy(alpha = 0.6f), modifier = Modifier.size(20.dp))
  }
}
