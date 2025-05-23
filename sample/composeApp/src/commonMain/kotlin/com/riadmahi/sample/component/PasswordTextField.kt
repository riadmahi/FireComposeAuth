package com.riadmahi.sample.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import firecomposeauth.sample.composeapp.generated.resources.Res
import firecomposeauth.sample.composeapp.generated.resources.ic_eye
import firecomposeauth.sample.composeapp.generated.resources.ic_eye_slash
import org.jetbrains.compose.resources.painterResource


@Composable
fun PasswordTextField(password: String, onPasswordChange: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = password,
        onValueChange = onPasswordChange,
        placeholder = { Text(text = "Mot de passe") },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFEAE5D8),
            unfocusedContainerColor = Color(0xFFEAE5D8),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedPlaceholderColor = Color(0xFFA08F63),
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = if (passwordVisible) KeyboardType.Text else KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(),
        maxLines = 1,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Image(
                    painter = painterResource(
                        if (passwordVisible) Res.drawable.ic_eye_slash else Res.drawable.ic_eye
                    ),
                    contentDescription = if (passwordVisible) "Masquer le mot de passe" else "Afficher le mot de passe",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}