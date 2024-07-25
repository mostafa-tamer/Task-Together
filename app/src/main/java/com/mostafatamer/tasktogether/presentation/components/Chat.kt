package com.mostafatamer.tasktogether.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mostafatamer.tasktogether.DefaultShape
import com.mostafatamer.tasktogether.domain.model.Message
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(text: String, actions: @Composable RowScope.() -> Unit = {}) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),
        title = {
            Text(
                text = text,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        actions = actions
    )
}

@Composable
fun MessageSending(
    sendMessage: (message: String) -> Unit,
) {
    var message by remember { mutableStateOf("") }

    val primaryColor = MaterialTheme.colorScheme.primary

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .background(Color.Transparent)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            value = message,
            singleLine = true,
            onValueChange = {
                message = it
            },
            placeholder = {
                Text(text = "Enter your message")
            },
            colors = OutlinedTextFieldDefaults.colors(), shape = RoundedCornerShape(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Card(
            shape = CircleShape,
        ) {
            IconButton(
                modifier = Modifier
                    .background(Color.Gray)
                    .background(primaryColor),
                onClick = {
                    if (message.trim().isNotEmpty()) {
                        sendMessage.invoke(message)
                        message = ""
                    }
                }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Send,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun ProgressIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
    }
}

fun timeMillisConverter(timeMillis: Long): String {
    val dateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(timeMillis), ZoneOffset.UTC
    )

    return dateTime.format(
        DateTimeFormatter.ofPattern("hh:mm a")
    )
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun Message(message: Message, isMyMessage: Boolean) {
    val groupColleague = if (!isMyMessage) message.sender.nickname else null

    val timeString by remember {
        mutableStateOf(
            timeMillisConverter(
                message.timestamp.time
            )
        )
    }

    val screenWidth = LocalConfiguration.current.screenWidthDp * 0.75


        Spacer(modifier = Modifier.width(8.dp))
        Card(
            modifier = Modifier
                .sizeIn(maxWidth = screenWidth.dp),
            shape = DefaultShape
        ) {
            Box(
                Modifier
                    .background(
                        if (isMyMessage) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondary
                    )
                    .padding(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    if (groupColleague != null) {
                        Text(
                            text = groupColleague, fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = message.content.trim(), fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = timeString,
                        textAlign = TextAlign.End,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }


}

