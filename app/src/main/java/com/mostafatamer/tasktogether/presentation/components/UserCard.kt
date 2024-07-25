package com.mostafatamer.tasktogether.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mostafatamer.tasktogether.DefaultShape
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.UserSelection


@Composable
fun UserSelectionCard(userSelection: UserSelection, onClick: () -> Unit) {
    Card(shape = DefaultShape) {
        Box(
            modifier = Modifier
                .clickable(onClick = onClick)
                .fillMaxSize()
                .padding(16.dp)
        )
        {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = userSelection.user.nickname,
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp
                )
                if (userSelection.isSelected) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
