package com.example.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRose
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.UltraVideoViewModel

@Composable
fun AuthScreen(
  viewModel: UltraVideoViewModel,
  onLoginSuccess: () -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Login, 1: Sign Up

  var email by remember { mutableStateOf("faizanansari009933@gmail.com") }
  var password by remember { mutableStateOf("UltraVideo2026!") }
  var name by remember { mutableStateOf("Faizan Ansari") }
  var rememberLogin by remember { mutableStateOf(true) }
  var passwordVisible by remember { mutableStateOf(false) }

  // Forgot password modal
  var showForgotDialog by remember { mutableStateOf(false) }
  var forgotEmail by remember { mutableStateOf("") }
  var forgotNewPass by remember { mutableStateOf("") }

  val authError by viewModel.authError.collectAsState()
  val authSuccess by viewModel.authSuccessMessage.collectAsState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(DarkCanvas)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 32.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(20.dp))

      // App Logo & Header
      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(
              colors = listOf(NeonViolet, Color(0xFF1B1A3A))
            )
          )
          .border(2.dp, NeonCyan.copy(alpha = 0.6f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = "Logo",
          tint = NeonCyan,
          modifier = Modifier.size(36.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Ultra Video AI",
        style = MaterialTheme.typography.headlineMedium.copy(
          fontWeight = FontWeight.ExtraBold,
          color = TextPrimary,
          letterSpacing = 1.sp
        )
      )

      Text(
        text = "AI Video Enhancer & Professional Editor",
        style = MaterialTheme.typography.bodySmall.copy(
          color = NeonCyan,
          fontWeight = FontWeight.Medium
        )
      )

      Spacer(modifier = Modifier.height(28.dp))

      // Tab selector: Login vs Sign Up
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = DarkSurfaceElevated,
        contentColor = NeonCyan,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
            color = NeonViolet
          )
        },
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(12.dp))
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = {
            selectedTab = 0
            viewModel.clearAuthMessages()
          },
          text = {
            Text(
              text = "Log In",
              fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
              color = if (selectedTab == 0) TextPrimary else TextSecondary
            )
          }
        )
        Tab(
          selected = selectedTab == 1,
          onClick = {
            selectedTab = 1
            viewModel.clearAuthMessages()
          },
          text = {
            Text(
              text = "Sign Up",
              fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
              color = if (selectedTab == 1) TextPrimary else TextSecondary
            )
          }
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Error and Success alerts
      AnimatedVisibility(visible = authError != null) {
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
          color = NeonRose.copy(alpha = 0.15f),
          border = androidx.compose.foundation.BorderStroke(1.dp, NeonRose.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(10.dp)
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.ErrorOutline, contentDescription = "Error", tint = NeonRose)
            Text(text = authError ?: "", color = TextPrimary, fontSize = 13.sp)
          }
        }
      }

      AnimatedVisibility(visible = authSuccess != null) {
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
          color = NeonEmerald.copy(alpha = 0.15f),
          border = androidx.compose.foundation.BorderStroke(1.dp, NeonEmerald.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(10.dp)
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = "Success", tint = NeonEmerald)
            Text(text = authSuccess ?: "", color = TextPrimary, fontSize = 13.sp)
          }
        }
      }

      // Card container for form
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = DarkSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          if (selectedTab == 1) {
            // Sign up name field
            OutlinedTextField(
              value = name,
              onValueChange = { name = it },
              label = { Text("Full Name", color = TextSecondary) },
              leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name", tint = NeonViolet) },
              singleLine = true,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("signup_name_field"),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonViolet,
                unfocusedBorderColor = DarkSurfaceBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
              ),
              shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
          }

          // Email field
          OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address", color = TextSecondary) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = NeonCyan) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("auth_email_field"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = NeonCyan,
              unfocusedBorderColor = DarkSurfaceBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Password field
          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = TextSecondary) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password", tint = NeonViolet) },
            trailingIcon = {
              IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                  imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                  contentDescription = "Toggle password",
                  tint = TextSecondary
                )
              }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("auth_password_field"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = NeonViolet,
              unfocusedBorderColor = DarkSurfaceBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Remember login & Forgot Password row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Checkbox(
                checked = rememberLogin,
                onCheckedChange = { rememberLogin = it },
                colors = CheckboxDefaults.colors(
                  checkedColor = NeonViolet,
                  uncheckedColor = TextSecondary,
                  checkmarkColor = Color.White
                ),
                modifier = Modifier.testTag("remember_login_checkbox")
              )
              Text(
                text = "Remember Login",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
              )
            }

            if (selectedTab == 0) {
              TextButton(
                onClick = {
                  forgotEmail = email
                  showForgotDialog = true
                },
                modifier = Modifier.testTag("forgot_password_button")
              ) {
                Text(
                  text = "Forgot?",
                  color = NeonCyan,
                  style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Primary Submit Button
          Button(
            onClick = {
              if (selectedTab == 0) {
                viewModel.login(email, password, rememberLogin)
              } else {
                viewModel.signUp(name, email, password, rememberLogin)
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("auth_submit_button"),
            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text(
              text = if (selectedTab == 0) "Log In to Ultra Video" else "Create AI Account",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Divider
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Box(
              modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(DarkSurfaceBorder)
            )
            Text(
              text = "  OR CONTINUE WITH  ",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextTertiary,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
              )
            )
            Box(
              modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(DarkSurfaceBorder)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Google Sign-In button
          OutlinedButton(
            onClick = {
              viewModel.googleSignIn("faizanansari009933@gmail.com", "Faizan Ansari")
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("google_signin_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = DarkSurfaceElevated,
              contentColor = TextPrimary
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              // Google color badge
              Box(
                modifier = Modifier
                  .size(20.dp)
                  .clip(CircleShape)
                  .background(Color.White),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "G",
                  fontWeight = FontWeight.Black,
                  color = Color(0xFF4285F4),
                  fontSize = 13.sp
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = "Continue with Google",
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(30.dp))

      // Creator branding badge
      Text(
        text = "Made by Faizan Ansari",
        style = MaterialTheme.typography.labelSmall.copy(
          color = TextSecondary,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 1.sp
        )
      )
    }

    // Forgot Password Dialog
    if (showForgotDialog) {
      AlertDialog(
        onDismissRequest = { showForgotDialog = false },
        containerColor = DarkSurfaceElevated,
        shape = RoundedCornerShape(18.dp),
        title = {
          Text("Reset Password", fontWeight = FontWeight.Bold, color = TextPrimary)
        },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
              "Enter your registered email and a new password to recover access.",
              color = TextSecondary,
              fontSize = 14.sp
            )
            OutlinedTextField(
              value = forgotEmail,
              onValueChange = { forgotEmail = it },
              label = { Text("Email", color = TextSecondary) },
              singleLine = true,
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = DarkSurfaceBorder,
                focusedTextColor = TextPrimary
              )
            )
            OutlinedTextField(
              value = forgotNewPass,
              onValueChange = { forgotNewPass = it },
              label = { Text("New Password", color = TextSecondary) },
              singleLine = true,
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonViolet,
                unfocusedBorderColor = DarkSurfaceBorder,
                focusedTextColor = TextPrimary
              )
            )
          }
        },
        confirmButton = {
          Button(
            onClick = {
              if (forgotEmail.isNotBlank() && forgotNewPass.isNotBlank()) {
                viewModel.forgotPassword(forgotEmail, forgotNewPass)
                showForgotDialog = false
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
          ) {
            Text("Update Password", color = TextPrimary)
          }
        },
        dismissButton = {
          TextButton(onClick = { showForgotDialog = false }) {
            Text("Cancel", color = TextSecondary)
          }
        }
      )
    }
  }
}

val TextTertiary = Color(0xFF6B7280)
