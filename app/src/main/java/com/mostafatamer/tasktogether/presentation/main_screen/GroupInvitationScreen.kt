package com.mostafatamer.tasktogether.presentation.main_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mostafatamer.tasktogether.R
import com.mostafatamer.tasktogether.SpacerBetweenCards
import com.mostafatamer.tasktogether.presentation.components.ActionBar
import com.mostafatamer.tasktogether.presentation.components.FastSwipe
import com.mostafatamer.tasktogether.presentation.main_screen.view_model.GroupInvitationViewModel


@Composable
fun GroupInvitationScreen(viewModel: GroupInvitationViewModel) {

    LaunchedEffect(Unit) {
        viewModel.reset()
        viewModel.loadInvitations()
    }

    FastSwipe(
        onRefresh = {
            viewModel.reset()
            viewModel.loadInvitations {
                it()
            }
        }
    ) {
        Scaffold(
            topBar = { TopBar() }
        ) {

            Content(it, viewModel)
        }
    }
}

@Composable
private fun Content(
    it: PaddingValues,
    viewModel: GroupInvitationViewModel,
) {
    Box(
        modifier = Modifier
            .padding(it)
            .padding(horizontal = 8.dp)
            .fillMaxSize()
    ) {
        if (viewModel.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary
            )
        }

        if (viewModel.isThereNoFriendRequests) {
            Text(
                text = "No invitations yet",
                modifier = Modifier.align(Alignment.Center)
            )
        }

        FriendRequestList(viewModel)
    }
}


@Composable
private fun TopBar() {
    ActionBar(label = "Group Invitations")
}


@Composable
private fun FriendRequestList(viewModel: GroupInvitationViewModel) {
    LazyColumn(Modifier.fillMaxSize()) {

        item {
            SpacerBetweenCards()
        }

        items(viewModel.groupInvitations) { groupInvitation ->
            Row(
                Modifier.padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(Modifier.weight(1f)) {
                    Text(
                        text = groupInvitation.group?.name ?: "null",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = groupInvitation.message ?: "")
                }
                Row {
                    val context = LocalContext.current

                    Action(true) {
                        viewModel.acceptInvitation(groupInvitation) {
                            if (it) {
                                Toast.makeText(
                                    context,
                                    "Invitation accepted",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error accepting invitation",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Action(false) {
                        viewModel.rejectInvitation(groupInvitation) {
                            if (it) {
                                Toast.makeText(
                                    context,
                                    "Invitation rejected",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error rejecting invitation",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        item {
            SpacerBetweenCards()
        }
    }
}

@Composable
private fun Action(
    isAccept: Boolean,
    onClick: () -> Unit,
) {
    Surface(shape = CircleShape) {
        IconButton(
            onClick = {
                onClick()
            },
            modifier = Modifier.background(
                if (isAccept) MaterialTheme.colorScheme.primary else Color.Red
            )

        ) {
            if (isAccept) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.wrong_delete_remove_trash_minus_cancel_close_2_svgrepo_com),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

