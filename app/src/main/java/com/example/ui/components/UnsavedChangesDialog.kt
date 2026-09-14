package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonRose
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun UnsavedChangesDialog(
  onSaveAndExit: () -> Unit,
  onDiscardAndExit: () -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    shape = RoundedCornerShape(20.dp),
    containerColor = DarkSurfaceElevated,
    icon = {
      Icon(
        imageVector = Icons.Default.WarningAmber,
        contentDescription = "Unsaved Changes Alert",
        tint = NeonAmber
      )
    },
    title = {
      Text(
        text = "Unsaved Changes",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
      )
    },
    text = {
      Column {
        Text(
          text = "You have unsaved changes. Do you want to exit?",
          style = MaterialTheme.typography.bodyMedium.copy(
            color = TextSecondary,
            fontSize = 15.sp
          )
        )
      }
    },
    confirmButton = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = onSaveAndExit,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("save_project_button"),
          colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("Save Project", fontWeight = FontWeight.SemiBold, color = TextPrimary)
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = onDiscardAndExit,
            modifier = Modifier
              .weight(1f)
              .testTag("exit_discard_button"),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonRose),
            border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(NeonRose.copy(alpha = 0.5f))),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("Exit", color = NeonRose, fontWeight = FontWeight.Medium)
          }

          TextButton(
            onClick = onDismiss,
            modifier = Modifier
              .weight(1f)
              .testTag("cancel_exit_button"),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("Cancel", color = TextSecondary, fontWeight = FontWeight.Medium)
          }
        }
      }
    },
    dismissButton = {}
  )
}
