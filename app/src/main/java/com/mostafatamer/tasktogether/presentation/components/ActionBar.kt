package com.mostafatamer.tasktogether.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mostafatamer.tasktogether.ACTION_BAR_HEIGHT


@Composable
fun ActionBar(
    label: String,
    prefixComposable: (@Composable () -> Unit)? = null,
    suffixComposable: (@Composable RowScope.() -> Unit)? = null,
    overlay: (@Composable () -> Unit)? = null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(4.dp)
            .height(ACTION_BAR_HEIGHT)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            prefixComposable?.invoke()
            HorizontalSpacer(widthDp = 8)
            Text(
                text = label,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
            HorizontalSpacer(8)

            suffixComposable?.invoke(this)
        }
        overlay?.invoke()
    }
}

@Composable
fun ActionBar(
    label: @Composable () -> Unit,
    prefixComposable: (@Composable () -> Unit)? = null,
    suffixComposable: (@Composable RowScope.() -> Unit)? = null,
    overlay: (@Composable () -> Unit)? = null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(4.dp)
            .height(ACTION_BAR_HEIGHT)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            prefixComposable?.invoke()
            HorizontalSpacer(widthDp = 8)
            label.invoke()
            HorizontalSpacer(8)
            suffixComposable?.invoke(this)
        }
        overlay?.invoke()
    }
}

@Composable
fun ActionBarLabel(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onPrimary
    )
}