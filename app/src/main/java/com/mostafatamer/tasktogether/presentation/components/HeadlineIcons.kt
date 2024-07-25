package com.mostafatamer.tasktogether.presentation.components

import androidx.compose.foundation.background
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.mostafatamer.tasktogether.DefaultShape

@Composable
fun HeadlineIcons(
    imageVector: ImageVector,
    contentDescription: String?,
    clickListener: () -> Unit
) {
    Card(
        shape = DefaultShape,
    ) {
        IconButton(
            modifier = Modifier.background(MaterialTheme.colorScheme.secondary),
            onClick = clickListener
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}
