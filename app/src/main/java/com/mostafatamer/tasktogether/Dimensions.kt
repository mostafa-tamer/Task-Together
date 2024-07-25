package com.mostafatamer.tasktogether

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val DefaultShape = RoundedCornerShape(8.dp)

val SpaceBetweenCards = 8.dp
val HorizontalPadding = 4.dp
val TopPadding = 8.dp
val BottomPadding = 8.dp


@Composable
fun SpacerBetweenCards() {
    Spacer(modifier = Modifier.height(SpaceBetweenCards))
}

class Dimensions {
    companion object {
        const val TOP_BAR_HEIGHT = 56
        const val SCREEN_PADDING = 8
        const val CARD_ROUNDNESS = 0
        const val SPACE_BETWEEN_CARDS = 4
        const val DEFAULT_TEXT_HEIGHT = 84
    }
}