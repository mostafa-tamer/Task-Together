package com.mostafatamer.tasktogether.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun TitleBar(
    text: String,
    content: @Composable RowScope.() -> Unit = {},
) {

    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            HorizontalSpacer(widthDp = 8)
            Box {
                Row { content.invoke(this) }
            }
        }
    }
    VerticalSpacer(heightDp = 16)
}
