package com.example.ui.screens.home

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ProjectEntity
import com.example.domain.model.ScreenRoute
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRose
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.UltraVideoViewModel

data class DashboardItem(
  val route: ScreenRoute,
  val title: String,
  val subtitle: String,
  val icon: ImageVector,
  val accentColor: Color,
  val testTag: String
)

@Composable
fun HomeScreen(
  viewModel: UltraVideoViewModel,
  onNavigate: (ScreenRoute) -> Unit
) {
  val user by viewModel.activeUser.collectAsState()
  val activeProject by viewModel.activeProject.collectAsState()
  val projects by viewModel.projectsList.collectAsState()

  val dashboardCards = listOf(
    DashboardItem(
      route = ScreenRoute.AI_ENHANCE,
      title = "AI Video Enhance",
      subtitle = "Denoise, Deblur & 8K Upscale",
      icon = Icons.Default.AutoAwesome,
      accentColor = NeonViolet,
      testTag = "nav_card_ai_enhance"
    ),
    DashboardItem(
      route = ScreenRoute.VIDEO_EDITOR,
      title = "Video Editor",
      subtitle = "Cut, Trim, Speed & Rotate",
      icon = Icons.Default.ContentCut,
      accentColor = NeonCyan,
      testTag = "nav_card_video_editor"
    ),
    DashboardItem(
      route = ScreenRoute.FACE_TOOLS,
      title = "Face Tools",
      subtitle = "Detection, Tracking & Restore",
      icon = Icons.Default.Face,
      accentColor = NeonEmerald,
      testTag = "nav_card_face_tools"
    ),
    DashboardItem(
      route = ScreenRoute.ADD_LOGO,
      title = "Watermark Studio",
      subtitle = "Upload Image or Generate Logo",
      icon = Icons.Default.WaterDrop,
      accentColor = NeonAmber,
      testTag = "nav_card_add_logo"
    ),
    DashboardItem(
      route = ScreenRoute.ADD_TEXT,
      title = "Add Text",
      subtitle = "Typography, Fonts & Neon FX",
      icon = Icons.Default.TextFields,
      accentColor = NeonRose,
      testTag = "nav_card_add_text"
    ),
    DashboardItem(
      route = ScreenRoute.MY_PROJECTS,
      title = "My Projects",
      subtitle = "${projects.size} Saved Projects",
      icon = Icons.Default.Folder,
      accentColor = Color(0xFF4285F4),
      testTag = "nav_card_my_projects"
    ),
    DashboardItem(
      route = ScreenRoute.PROFILE,
      title = "Profile",
      subtitle = user?.name ?: "Faizan Ansari",
      icon = Icons.Default.AccountCircle,
      accentColor = Color(0xFFA855F7),
      testTag = "nav_card_profile"
    ),
    DashboardItem(
      route = ScreenRoute.SETTINGS,
      title = "Settings",
      subtitle = "Theme, Codec & About",
      icon = Icons.Default.Settings,
      accentColor = Color(0xFF00B0FF),
      testTag = "nav_card_settings"
    )
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(DarkCanvas)
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top App Bar / Profile Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(NeonViolet)
              .border(1.5.dp, NeonCyan, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = "Logo",
              tint = Color.White,
              modifier = Modifier.size(22.dp)
            )
          }

          Column {
            Text(
              text = "Ultra Video AI",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                letterSpacing = 0.5.sp
              )
            )
            Text(
              text = "Made by Faizan Ansari",
              style = MaterialTheme.typography.labelSmall.copy(
                color = NeonCyan,
                fontWeight = FontWeight.SemiBold
              )
            )
          }
        }

        // Profile Avatar Button
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = DarkSurfaceElevated,
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
          modifier = Modifier.clickable { onNavigate(ScreenRoute.PROFILE) }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(NeonViolet),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = (user?.name?.firstOrNull() ?: 'F').toString(),
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Color.White,
                  fontWeight = FontWeight.Bold
                )
              )
            }
            Text(
              text = user?.tier ?: "Ultra Pro",
              style = MaterialTheme.typography.labelSmall.copy(
                color = NeonAmber,
                fontWeight = FontWeight.Bold
              )
            )
          }
        }
      }
    }

    // Hero Active Studio Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(1.dp, NeonViolet.copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.radialGradient(
                colors = listOf(NeonViolet.copy(alpha = 0.25f), Color.Transparent),
                center = androidx.compose.ui.geometry.Offset(200f, 100f),
                radius = 600f
              )
            )
            .padding(18.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = NeonEmerald.copy(alpha = 0.18f),
              border = androidx.compose.foundation.BorderStroke(1.dp, NeonEmerald.copy(alpha = 0.4f))
            ) {
              Text(
                text = "● NEURAL ENGINE READY",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                  color = NeonEmerald,
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp
                )
              )
            }

            Text(
              text = activeProject.resolution,
              style = MaterialTheme.typography.labelSmall.copy(
                color = NeonCyan,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = activeProject.title,
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          )

          Text(
            text = "Duration: ${activeProject.durationSeconds}s • Aspect: ${activeProject.aspectRatio} • Speed: ${activeProject.videoSpeed}x",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
          )

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = { onNavigate(ScreenRoute.AI_ENHANCE) },
              modifier = Modifier
                .weight(1f)
                .testTag("hero_enhance_button"),
              colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(Icons.Default.AutoAwesome, contentDescription = "Enhance", modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("AI Enhance", fontWeight = FontWeight.Bold)
            }

            Button(
              onClick = { onNavigate(ScreenRoute.VIDEO_EDITOR) },
              modifier = Modifier
                .weight(1f)
                .testTag("hero_editor_button"),
              colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated),
              border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(Icons.Default.ContentCut, contentDescription = "Edit", tint = NeonCyan, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Open Editor", color = TextPrimary, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // Export Banner CTA
    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigate(ScreenRoute.EXPORT) }
          .testTag("quick_export_button"),
        shape = RoundedCornerShape(14.dp),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.35f))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(NeonCyan.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Upload, contentDescription = "Export", tint = NeonCyan, modifier = Modifier.size(20.dp))
            }
            Column {
              Text(
                text = "Export Master Video",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
              )
              Text(
                text = "Render in 720p, 1080p, 2K, 4K, or 8K Ultra HD",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
              )
            }
          }

          Text(
            text = "Export →",
            color = NeonCyan,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }
    }

    // Dashboard 8 Core Feature Navigation Cards
    item {
      Text(
        text = "AI Video Studio Modules",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        ),
        modifier = Modifier.padding(top = 8.dp)
      )
    }

    // Render cards in pairs of 2
    val pairs = dashboardCards.chunked(2)
    items(pairs) { rowItems ->
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        for (item in rowItems) {
          DashboardFeatureCard(
            item = item,
            modifier = Modifier.weight(1f),
            onClick = { onNavigate(item.route) }
          )
        }
      }
    }

    // Recent Projects Section
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Recent Projects",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        )
        Text(
          text = "View All (${projects.size})",
          style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.SemiBold),
          modifier = Modifier.clickable { onNavigate(ScreenRoute.MY_PROJECTS) }
        )
      }
    }

    if (projects.isEmpty()) {
      item {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = DarkSurface,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "No saved projects yet. Start by enhancing or editing a video!",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(16.dp)
          )
        }
      }
    } else {
      items(projects.take(3)) { proj ->
        RecentProjectItem(
          project = proj,
          onOpen = {
            viewModel.loadProject(proj)
          },
          onDelete = {
            viewModel.deleteProject(proj.id)
          }
        )
      }
    }

    // Bottom Creator Signature
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Box(
        modifier = Modifier.fillMaxWidth(),
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

@Composable
fun DashboardFeatureCard(
  item: DashboardItem,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Card(
    modifier = modifier
      .height(124.dp)
      .clip(RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .testTag(item.testTag),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = DarkSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(14.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(item.accentColor.copy(alpha = 0.16f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = item.icon,
          contentDescription = item.title,
          tint = item.accentColor,
          modifier = Modifier.size(22.dp)
        )
      }

      Column {
        Text(
          text = item.title,
          style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        )
        Text(
          text = item.subtitle,
          style = MaterialTheme.typography.bodySmall.copy(
            color = TextSecondary,
            fontSize = 11.sp
          ),
          maxLines = 1
        )
      }
    }
  }
}

@Composable
fun RecentProjectItem(
  project: ProjectEntity,
  onOpen: () -> Unit,
  onDelete: () -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .clickable { onOpen() },
    shape = RoundedCornerShape(14.dp),
    color = DarkSurface,
    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
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
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
              Brush.linearGradient(
                listOf(NeonViolet.copy(alpha = 0.6f), Color(0xFF1B1232))
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Movie, contentDescription = "Video", tint = NeonCyan, modifier = Modifier.size(22.dp))
        }

        Column {
          Text(
            text = project.title,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          )
          Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = NeonViolet.copy(alpha = 0.2f)
            ) {
              Text(
                text = project.resolution,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontSize = 9.sp)
              )
            }
            Text(
              text = "${project.durationSeconds}s",
              style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
            )
          }
        }
      }

      Button(
        onClick = onOpen,
        colors = ButtonDefaults.buttonColors(containerColor = NeonViolet.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
      ) {
        Text("Open", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
      }
    }
  }
}
