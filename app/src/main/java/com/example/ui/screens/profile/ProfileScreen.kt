package com.example.ui.screens.profile

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.components.UltraTopAppBar
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

@Composable
fun ProfileScreen(
  viewModel: UltraVideoViewModel,
  onBackClick: () -> Unit
) {
  val user by viewModel.activeUser.collectAsState()
  val projects by viewModel.projectsList.collectAsState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DarkCanvas)
  ) {
    UltraTopAppBar(
      title = "User Profile",
      onBackClick = onBackClick
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Profile Avatar Card
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                Brush.radialGradient(
                  colors = listOf(NeonViolet.copy(alpha = 0.2f), Color.Transparent),
                  center = androidx.compose.ui.geometry.Offset(250f, 150f),
                  radius = 500f
                )
              )
              .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(
                  Brush.linearGradient(
                    listOf(NeonViolet, Color(0xFF26104F))
                  )
                )
                .border(2.5.dp, NeonCyan, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = (user?.name?.firstOrNull() ?: 'F').toString(),
                style = MaterialTheme.typography.headlineLarge.copy(
                  color = Color.White,
                  fontWeight = FontWeight.ExtraBold
                )
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = user?.name ?: "Faizan Ansari",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            )

            Text(
              text = user?.email ?: "faizanansari009933@gmail.com",
              style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
              shape = RoundedCornerShape(20.dp),
              color = NeonAmber.copy(alpha = 0.18f),
              border = androidx.compose.foundation.BorderStroke(1.dp, NeonAmber.copy(alpha = 0.5f))
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(Icons.Default.WorkspacePremium, contentDescription = "Tier", tint = NeonAmber, modifier = Modifier.size(16.dp))
                Text(
                  text = "${user?.tier ?: "Ultra Pro"} • Unlimited 8K",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = NeonAmber,
                    fontWeight = FontWeight.Bold
                  )
                )
              }
            }
          }
        }
      }

      // Monthly AI Quota & Neural Stats
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Memory, contentDescription = "AI Quota", tint = NeonCyan, modifier = Modifier.size(20.dp))
                Text("AI Neural GPU Acceleration", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
              }
              Text("32 / 100", style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
              progress = { 0.32f },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = NeonCyan,
              trackColor = DarkSurfaceBorder
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "High-speed 8K Super-Res passes remaining this billing cycle",
              style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
            )
          }
        }
      }

      // Account Details Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            ProfileDetailRow(label = "Session Security", value = "Encrypted Local SQLite / Room")
            ProfileDetailRow(label = "Saved Projects Count", value = "${projects.size} Active Projects")
            ProfileDetailRow(label = "Render Codec Engine", value = "Hardware H.265 / Apple ProRes")
            ProfileDetailRow(label = "App Build", value = "v1.0.0 (Production Release)")
          }
        }
      }

      // Logout Action
      item {
        Button(
          onClick = { viewModel.logout() },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("profile_logout_button"),
          colors = ButtonDefaults.buttonColors(containerColor = NeonRose.copy(alpha = 0.2f)),
          border = androidx.compose.foundation.BorderStroke(1.dp, NeonRose.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = NeonRose, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Log Out of Account", color = NeonRose, fontWeight = FontWeight.Bold)
        }
      }

      // Creator Branding
      item {
        Box(
          modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Ultra Video AI • Made by Faizan Ansari",
            style = MaterialTheme.typography.labelSmall.copy(
              color = TextSecondary,
              fontWeight = FontWeight.SemiBold,
              letterSpacing = 1.sp
            )
          )
        }
      }
    }
  }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(label, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
    Text(value, style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
  }
}
