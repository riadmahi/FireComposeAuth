package com.riadmahi.sample.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import firecomposeauth.sample.composeapp.generated.resources.Res
import firecomposeauth.sample.composeapp.generated.resources.ic_arrow_left
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String? = null,
    color: Color = MaterialTheme.colorScheme.onSurface,
    navigateBack: () -> Unit
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        title = {
            title?.let {
                Spacer(Modifier.width(12.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 16.sp
                )
            }
        },
        navigationIcon = {
            Image(
                painterResource(Res.drawable.ic_arrow_left),
                contentDescription = Res.drawable.ic_arrow_left.toString(),
                colorFilter = ColorFilter.tint(color),
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .clickable { navigateBack() }
            )
        }
    )
}