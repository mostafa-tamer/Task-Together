package com.mostafatamer.tasktogether.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.mostafatamer.tasktogether.DefaultShape

@Composable
fun NormalTextField(username: MutableState<String>, title: String, placeholder: String) {
    Text(text = title, fontWeight = FontWeight.Bold)
    VerticalSpacer(heightDp = 8)
    OutlinedTextField(
        shape = DefaultShape,
        singleLine = true,
        placeholder = { Text(text = placeholder) },
        modifier = Modifier.fillMaxWidth(),
        value = username.value,
        onValueChange = { u ->
            username.value = u
        }
    )
}

@Composable
fun PasswordTextField(text: MutableState<String>, title: String, placeholder: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold

    )
    VerticalSpacer(heightDp = 8)
    OutlinedTextField(
        shape = DefaultShape,
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        placeholder = {
            Text(text = placeholder)
        },
        modifier = Modifier.fillMaxWidth(),
        value = text.value, onValueChange = { password ->
            text.value = password
        }
    )
}
