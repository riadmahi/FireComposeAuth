package com.riadmahi.sample

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun getPlatformContext(): Any? = LocalContext.current