package com.mostafatamer.tasktogether.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mostafatamer.tasktogether.Dimensions


@Composable
fun HorizontalSpacer(widthDp: Int) {
    Spacer(modifier = Modifier.width(widthDp.dp))
}

@Composable
fun VerticalSpacer(heightDp: Int) {
    Spacer(modifier = Modifier.height(heightDp.dp))
}

@Composable
fun <T> TrailingVerticalSpacer(
    list: List<T>,
    item: T,
    heightDp: Int = Dimensions.SPACE_BETWEEN_CARDS
) {
    if (list.indexOf(item) < list.size - 1) {
        VerticalSpacer(heightDp = heightDp)
    }
}