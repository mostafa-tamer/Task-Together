package com.mostafatamer.tasktogether.presentation.group_screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mostafatamer.tasktogether.DefaultShape
import com.mostafatamer.tasktogether.R
import com.mostafatamer.tasktogether.domain.model.User
import com.mostafatamer.tasktogether.presentation.authentication.appUser.AppUser
import com.mostafatamer.tasktogether.presentation.components.FastSwipe
import com.mostafatamer.tasktogether.presentation.components.IconCard
import com.mostafatamer.tasktogether.presentation.components.TitleBar
import com.mostafatamer.tasktogether.presentation.group_screen.viewModels.GroupConfigViewModel
import com.mostafatamer.tasktogether.showToast


@OptIn(ExperimentalCoilApi::class)
@Composable
fun GroupOverviewScreen(
    viewModel: GroupConfigViewModel,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.loadMembers() }

    FastSwipe(onRefresh = { viewModel.loadMembers { it() } }) {
        Box(
            Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Column {

                val showAddMemberDialog = remember { mutableStateOf(false) }

                AddMemberAlertDialog(showAddMemberDialog, viewModel, context)



                TitleBar(text = "Members") {
                    if (viewModel.group.adminUsername == AppUser.username) {
                        IconCard(imageVector = Icons.Filled.Add) {
                            showAddMemberDialog.value = true
                        }
                    }
                }

                LazyColumn(Modifier.fillMaxSize()) {
                    items(viewModel.members) {
                        Card(shape = DefaultShape) {
                            Row(
                                verticalAlignment = CenterVertically,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Surface(shape = CircleShape) {
                                    Image(
                                        painter = rememberImagePainter(it.photo),
                                        contentDescription = null,
                                        Modifier.size(48.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = it.nickname,

                                    fontSize = 24.sp
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Spacer(modifier = Modifier.weight(1f))

                                val showRemoveMemberDialog = remember { mutableStateOf(false) }

                                RemoveMemberAlertDialog(
                                    showRemoveMemberDialog,
                                    it,
                                    viewModel,
                                    context
                                )

                                if (viewModel.group.adminUsername == AppUser.username &&
                                    it.username != AppUser.username
                                ) {
                                    IconButton(onClick = {
                                        showRemoveMemberDialog.value = true
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_block_24),
                                            contentDescription = null
                                        )
                                    }
                                }

                                if (it.username == viewModel.group.adminUsername) {
                                    IconButton(
                                        onClick = { },
                                        enabled = false,
                                        colors = IconButtonDefaults.iconButtonColors(
                                            disabledContentColor = Color(0xFFFB8B24)
                                        )
                                    ) {

                                        Icon(
                                            painter = painterResource(id = R.drawable.crown_svgrepo_com),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}


@Composable
private fun AddMemberAlertDialog(
    showAddMemberDialog: MutableState<Boolean>,
    viewModel: GroupConfigViewModel,
    context: Context,
) {
    if (showAddMemberDialog.value) {
        var username by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = "Add Member")
            },
            text = {
                Column {
                    OutlinedTextField(value = username, onValueChange = { username = it },
                        placeholder = {
                            Text(text = "Username")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        minLines = 3, maxLines = 3,
                        placeholder = {
                            Text(text = "Message")
                        }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addMemberToGroup(username, message) {
                        if (it) {
                            showToast(context, "Invitation is sent")
                            showAddMemberDialog.value = false
                        } else {
                            showToast(context, "Failed to invite member")
                        }
                    }
                }) {
                    Text(text = "Send")
                }
            },
            dismissButton = {
                Button(onClick = { showAddMemberDialog.value = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}

@Composable
private fun RemoveMemberAlertDialog(
    showRemoveMemberDialog: MutableState<Boolean>,
    it: User,
    viewModel: GroupConfigViewModel,
    context: Context,
) {
    if (showRemoveMemberDialog.value) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = "Remove member")
            },
            text = {
                Text(text = "Are you sure you want to remove ${it.nickname} from the group?")
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.removeMemberFromGroup(it) {
                        if (it) {
                            showToast(context, "User removed successfully")
                            viewModel.loadMembers()
                            showRemoveMemberDialog.value = false
                        } else {
                            showToast(context, "Failed to remove user")
                        }
                    }
                }) {
                    Text(text = "Remove")
                }
            },
            dismissButton = {
                Button(onClick = { showRemoveMemberDialog.value = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}