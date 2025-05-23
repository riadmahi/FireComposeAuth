package com.riadmahi.sample.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomSnackBar(snackbarData: SnackbarData) {
    val isError = snackbarData.visuals.message.contains("error", ignoreCase = true)
    val backgroundColor = if (isError) Color(0xFFFD0D0D) else Color(0xFFEAE5D8)

    Snackbar(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp)),
        containerColor = backgroundColor,
        action = {
            TextButton(onClick = { snackbarData.dismiss() }) {
                Text(text = snackbarData.visuals.actionLabel ?: "OK", color = Color.White)
            }
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = snackbarData.visuals.message,
                color = if (isError) Color.White else MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            )
        }
    }
}