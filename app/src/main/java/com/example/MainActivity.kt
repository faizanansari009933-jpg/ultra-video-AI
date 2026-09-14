package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.model.ScreenRoute
import com.example.ui.components.UnsavedChangesDialog
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.editor.EditorScreen
import com.example.ui.screens.enhance.EnhanceScreen
import com.example.ui.screens.export.ExportScreen
import com.example.ui.screens.facetools.FaceToolsScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.logo.LogoScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.projects.ProjectsScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.text.TextScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.UltraVideoViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val ultraViewModel: UltraVideoViewModel = viewModel()
        val currentRoute by ultraViewModel.currentRoute.collectAsState()
        val activeUser by ultraViewModel.activeUser.collectAsState()
        val showUnsavedDialog by ultraViewModel.showUnsavedDialog.collectAsState()

        // System back button handling
        BackHandler(enabled = currentRoute != ScreenRoute.SPLASH) {
          val handled = ultraViewModel.handleBackNavigation()
          if (!handled) {
            finish()
          }
        }

        // Unsaved changes confirmation dialog
        if (showUnsavedDialog) {
          UnsavedChangesDialog(
            onSaveAndExit = { ultraViewModel.confirmSaveAndExit() },
            onDiscardAndExit = { ultraViewModel.confirmDiscardAndExit() },
            onDismiss = { ultraViewModel.dismissUnsavedDialog() }
          )
        }

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          when (currentRoute) {
            ScreenRoute.SPLASH -> {
              SplashScreen(
                onAnimationFinished = {
                  if (activeUser != null) {
                    ultraViewModel.navigateTo(ScreenRoute.HOME)
                  } else {
                    ultraViewModel.navigateTo(ScreenRoute.AUTH)
                  }
                }
              )
            }

            ScreenRoute.AUTH -> {
              AuthScreen(
                viewModel = ultraViewModel,
                onLoginSuccess = {
                  ultraViewModel.navigateTo(ScreenRoute.HOME)
                }
              )
            }

            ScreenRoute.HOME -> {
              HomeScreen(
                viewModel = ultraViewModel,
                onNavigate = { route -> ultraViewModel.navigateTo(route) }
              )
            }

            ScreenRoute.AI_ENHANCE -> {
              EnhanceScreen(
                viewModel = ultraViewModel,
                onBackClick = { ultraViewModel.handleBackNavigation() }
              )
            }

            ScreenRoute.VIDEO_EDITOR -> {
              EditorScreen(
                viewModel = ultraViewModel,
                onBackClick = { ultraViewModel.handleBackNavigation() }
              )
            }

            ScreenRoute.FACE_TOOLS -> {
              FaceToolsScreen(
                viewModel = ultraViewModel,
                onBackClick = { ultraViewModel.handleBackNavigation() }
              )
            }

            ScreenRoute.ADD_LOGO -> {
              LogoScreen(
                viewModel = ultraViewModel,
                onBackClick = { ultraViewModel.handleBackNavigation() }
              )
            }

            ScreenRoute.ADD_TEXT -> {
              TextScreen(
                viewModel = ultraViewModel,
                onBackClick = { ultraViewModel.handleBackNavigation() }
              )
            }

            ScreenRoute.MY_PROJECTS -> {
              ProjectsScreen(
                viewModel = ultraViewModel,
                onBackClick = { ultraViewModel.handleBackNavigation() }
              )
            }

            ScreenRoute.PROFILE -> {
              ProfileScreen(
                viewModel = ultraViewModel,
                onBackClick = { ultraViewModel.handleBackNavigation() }
              )
            }

            ScreenRoute.SETTINGS -> {
              SettingsScreen(
                viewModel = ultraViewModel,
                onBackClick = { ultraViewModel.handleBackNavigation() }
              )
            }

            ScreenRoute.EXPORT -> {
              ExportScreen(
                viewModel = ultraViewModel,
                onBackClick = { ultraViewModel.handleBackNavigation() }
              )
            }
          }
        }
      }
    }
  }
}

