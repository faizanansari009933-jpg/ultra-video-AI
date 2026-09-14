package com.example.ui.screens.projects

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ProjectEntity
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProjectsScreen(
  viewModel: UltraVideoViewModel,
  onBackClick: () -> Unit
) {
  val projects by viewModel.projectsList.collectAsState()
  var searchQuery by remember { mutableStateOf("") }
  var projectToDelete by remember { mutableStateOf<ProjectEntity?>(null) }
  var showNewProjectDialog by remember { mutableStateOf(false) }
  var newProjectTitle by remember { mutableStateOf("") }

  val filteredProjects = projects.filter {
    it.title.contains(searchQuery, ignoreCase = true) ||
      it.resolution.contains(searchQuery, ignoreCase = true)
  }

  val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DarkCanvas)
  ) {
    UltraTopAppBar(
      title = "My Projects",
      onBackClick = onBackClick,
      actions = {
        Button(
          onClick = {
            newProjectTitle = "Project ${projects.size + 1}"
            showNewProjectDialog = true
          },
          colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .padding(end = 8.dp)
            .testTag("new_project_button")
        ) {
          Icon(Icons.Default.Add, contentDescription = "New", modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("New Project", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
      // Search Box
      item {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search projects by name or resolution...", color = TextSecondary, fontSize = 13.sp) },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = NeonCyan) },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("projects_search_bar"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonCyan,
            unfocusedBorderColor = DarkSurfaceBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          ),
          shape = RoundedCornerShape(12.dp)
        )
      }

      // Stats Banner
      item {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = DarkSurfaceElevated,
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "LOCAL ROOM STORAGE: ${projects.size} PROJECTS",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
              )
            )
            Text(
              text = "Offline-Ready",
              color = NeonEmerald,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            )
          }
        }
      }

      if (filteredProjects.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(DarkSurface),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(Icons.Default.Folder, contentDescription = "Empty", tint = TextSecondary, modifier = Modifier.size(48.dp))
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = if (searchQuery.isEmpty()) "No projects found in database" else "No matching projects",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
              )
            }
          }
        }
      } else {
        items(filteredProjects, key = { it.id }) { proj ->
          ProjectCardItem(
            project = proj,
            dateString = dateFormat.format(Date(proj.lastModified)),
            onOpen = { viewModel.loadProject(proj) },
            onDelete = { projectToDelete = proj }
          )
        }
      }
    }

    // Delete Confirmation Dialog
    if (projectToDelete != null) {
      AlertDialog(
        onDismissRequest = { projectToDelete = null },
        containerColor = DarkSurfaceElevated,
        shape = RoundedCornerShape(18.dp),
        title = {
          Text("Delete Project?", fontWeight = FontWeight.Bold, color = TextPrimary)
        },
        text = {
          Text(
            "Are you sure you want to delete '${projectToDelete?.title}'? This action cannot be undone.",
            color = TextSecondary,
            fontSize = 14.sp
          )
        },
        confirmButton = {
          Button(
            onClick = {
              projectToDelete?.let { viewModel.deleteProject(it.id) }
              projectToDelete = null
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonRose)
          ) {
            Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
          }
        },
        dismissButton = {
          TextButton(onClick = { projectToDelete = null }) {
            Text("Cancel", color = TextSecondary)
          }
        }
      )
    }

    // New Project Dialog
    if (showNewProjectDialog) {
      AlertDialog(
        onDismissRequest = { showNewProjectDialog = false },
        containerColor = DarkSurfaceElevated,
        shape = RoundedCornerShape(18.dp),
        title = {
          Text("Create New Project", fontWeight = FontWeight.Bold, color = TextPrimary)
        },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Enter a name for your new video project:", color = TextSecondary, fontSize = 13.sp)
            OutlinedTextField(
              value = newProjectTitle,
              onValueChange = { newProjectTitle = it },
              singleLine = true,
              modifier = Modifier.fillMaxWidth().testTag("new_project_title_input"),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = DarkSurfaceBorder,
                focusedTextColor = TextPrimary
              ),
              shape = RoundedCornerShape(10.dp)
            )
          }
        },
        confirmButton = {
          Button(
            onClick = {
              if (newProjectTitle.isNotBlank()) {
                viewModel.createNewProject(newProjectTitle)
                showNewProjectDialog = false
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
            modifier = Modifier.testTag("confirm_new_project_button")
          ) {
            Text("Create & Open", color = TextPrimary, fontWeight = FontWeight.Bold)
          }
        },
        dismissButton = {
          TextButton(onClick = { showNewProjectDialog = false }) {
            Text("Cancel", color = TextSecondary)
          }
        }
      )
    }
  }
}

@Composable
fun ProjectCardItem(
  project: ProjectEntity,
  dateString: String,
  onOpen: () -> Unit,
  onDelete: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable { onOpen() }
      .testTag("project_item_${project.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = DarkSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(
                Brush.linearGradient(
                  listOf(NeonViolet, Color(0xFF1B1A3F))
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Movie, contentDescription = "Video", tint = NeonCyan, modifier = Modifier.size(28.dp))
          }

          Column {
            Text(
              text = project.title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
              text = dateString,
              style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
            )
          }
        }

        IconButton(
          onClick = onDelete,
          modifier = Modifier.size(32.dp).testTag("delete_project_${project.id}")
        ) {
          Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextSecondary.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Badges
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = NeonViolet.copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonViolet.copy(alpha = 0.4f))
          ) {
            Text(
              text = project.resolution,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              style = MaterialTheme.typography.labelSmall.copy(
                color = NeonCyan,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp
              )
            )
          }

          Surface(
            shape = RoundedCornerShape(6.dp),
            color = DarkSurfaceElevated
          ) {
            Text(
              text = "${project.durationSeconds}s",
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp)
            )
          }

          Surface(
            shape = RoundedCornerShape(6.dp),
            color = DarkSurfaceElevated
          ) {
            Text(
              text = project.aspectRatio,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp)
            )
          }
        }

        Button(
          onClick = onOpen,
          colors = ButtonDefaults.buttonColors(containerColor = NeonCyan.copy(alpha = 0.15f)),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
        ) {
          Text("Open Studio", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
      }
    }
  }
}
