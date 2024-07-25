package com.mostafatamer.tasktogether.presentation.group_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mostafatamer.tasktogether.HorizontalPadding
import com.mostafatamer.tasktogether.R
import com.mostafatamer.tasktogether.paginationConfiguration
import com.mostafatamer.tasktogether.presentation.authentication.appUser.AppUser
import com.mostafatamer.tasktogether.presentation.components.EmptyList
import com.mostafatamer.tasktogether.presentation.components.Message
import com.mostafatamer.tasktogether.presentation.components.MessageSending
import com.mostafatamer.tasktogether.presentation.components.ProgressIndicator
import com.mostafatamer.tasktogether.presentation.group_screen.viewModels.GroupChatViewModel

@Composable
fun GroupChatScreen(
    viewModel: GroupChatViewModel,
) {
    val state = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.reset()
        viewModel.observeMessages { message ->
            if (message.sender.username == AppUser.nickname) {
                if (viewModel.messages.isNotEmpty()) {
                    state.scrollToItem(0)
                }
            }
        }
    }

    Content(viewModel, state)
}

@Composable
private fun Content(
    viewModel: GroupChatViewModel,
    state: LazyListState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scaffold {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = HorizontalPadding)
                ) {
                    if (viewModel.isThereNoMessages) {
                        EmptyList("Chat is empty")
                    }
                    Chat(viewModel, state)
                }

                MessageSending { message ->
                    viewModel.sendMessage(message)
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class)
@Composable
private fun Chat(
    viewModel: GroupChatViewModel,
    state: LazyListState,
) {

    LaunchedEffect(state) {
        paginationConfiguration(viewModel.paginationState, viewModel.messages, state) {
            viewModel.loadMessages()
        }
    }

    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        LazyColumn(
            Modifier.fillMaxSize(),
            state = state,
            reverseLayout = true,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            itemsIndexed(viewModel.messages) { index, messageDto ->
                var innerWidth by remember { mutableIntStateOf(0) }
                var innerHeight by remember { mutableIntStateOf(0) }


                val density = LocalDensity.current.density
                val isMyMessage = messageDto.sender.username == AppUser.username

                Box {
                    Box(
                        modifier = Modifier
                            .align(if (isMyMessage) Alignment.BottomStart else Alignment.BottomEnd)
                    ) {
                        Row {
                            val translatePainter =
                                painterResource(id = R.drawable.translate_fill_svgrepo_com)
                            val rollbackPainter = painterResource(id = R.drawable.back_svgrepo_com)
                            var translateIcon by remember {
                                mutableStateOf(
                                    if (viewModel.alreadyTranslated(messageDto))
                                        rollbackPainter else translatePainter
                                )
                            }


                            if (isMyMessage)
                                Image(
                                    painter = rememberImagePainter(messageDto.sender.photo),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                )

                            if (!isMyMessage) {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                )
                            }
                            if (!isMyMessage) {
                                Box(
                                    modifier = Modifier.size(
                                        width = if (innerWidth > 0) (innerWidth + 12).dp else Dp.Unspecified,
                                        height = if (innerHeight > 0) (innerHeight + 8).dp else Dp.Unspecified
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .onGloballyPositioned { coordinates ->
                                                innerWidth =
                                                    ((coordinates.size.width / density).toInt())
                                                innerHeight =
                                                    ((coordinates.size.height / density).toInt())
                                            }
                                            .align(Alignment.BottomEnd)
                                    ) { Message(messageDto, false) }
                                    IconButton(
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .size(width = 24.dp, height = 18.dp),
                                        onClick = {
                                            if (translateIcon == translatePainter) {
                                                viewModel.translateMessage(messageDto, index) {
                                                    if (it) {
                                                        translateIcon = rollbackPainter
                                                    }
                                                }
                                            } else {
                                                viewModel.translationRollback(messageDto, index)
                                                translateIcon = translatePainter
                                            }
                                        }) {
                                        Icon(
                                            painter = translateIcon,
                                            contentDescription = null,
                                            Modifier.size(12.dp)
                                        )
                                    }
                                }
                            } else {
                                Message(messageDto, isMyMessage)
                            }
                            if (isMyMessage)
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                )

                            if (!isMyMessage) {
                                Image(
                                    painter = rememberImagePainter(messageDto.sender.photo),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .size(24.dp)
                                        .clip(CircleShape)

                                )
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (viewModel.paginationState.isLoading) {
                item {
                    ProgressIndicator()
                }
            }
        }
    }
}


