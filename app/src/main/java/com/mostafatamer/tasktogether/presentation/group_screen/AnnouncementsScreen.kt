package com.mostafatamer.tasktogether.presentation.group_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mostafatamer.tasktogether.DefaultShape
import com.mostafatamer.tasktogether.SpacerBetweenCards
import com.mostafatamer.tasktogether.domain.model.Announcement
import com.mostafatamer.tasktogether.presentation.authentication.appUser.AppUser
import com.mostafatamer.tasktogether.presentation.components.FastSwipe
import com.mostafatamer.tasktogether.presentation.components.HeadlineIcons
import com.mostafatamer.tasktogether.presentation.components.HorizontalSpacer
import com.mostafatamer.tasktogether.presentation.components.TitleBar
import com.mostafatamer.tasktogether.presentation.components.TrailingVerticalSpacer
import com.mostafatamer.tasktogether.presentation.components.VerticalSpacer
import com.mostafatamer.tasktogether.presentation.group_screen.viewModels.AnnouncementViewModel
import kotlinx.coroutines.launch

@Composable
fun AnnouncementsScreen(
    viewModel: AnnouncementViewModel, navHostController: NavHostController,
) {
    LaunchedEffect(Unit) {
        viewModel.getAnnouncements()
    }

    FastSwipe(onRefresh = {
        viewModel.getAnnouncements { it() }
    }) {
        Box(
            Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 4.dp)
        ) {
            Body(viewModel)
        }
    }
}

@Composable
private fun TitleBar(
    viewModel: AnnouncementViewModel,
) {
    if (viewModel.showCreateAlert) {
        CreateEditAlertDialog(viewModel, "Create Announcement") { viewModel.createAnnouncement() }
    }

    Box {
        TitleBar("Announcements") {
            if (viewModel.group.adminUsername == AppUser.username) {
                Row {
                    HorizontalSpacer(widthDp = 8)
                    HeadlineIcons(
                        Icons.Filled.Add,
                        "Create Announcement",
                    ) {
                        viewModel.announcementState.clean()
                        viewModel.showCreateAlert = true
                    }
                }
            }
        }
    }
    VerticalSpacer(heightDp = 16)
}


@Composable
private fun Body(viewModel: AnnouncementViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val state = rememberLazyListState()

    LaunchedEffect(viewModel.announcements.size) {
        coroutineScope.launch {
            if (viewModel.announcements.isNotEmpty()) {
                state.scrollToItem(0)
            }
        }
    }
    AlertDialogs(viewModel)

    Column {
        TitleBar(viewModel)
        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxSize(),

        ) {
            item {
                SpacerBetweenCards()
            }
            itemsIndexed(viewModel.announcements) { index, announcement ->
                AnnouncementCard(index, announcement, viewModel)
                TrailingVerticalSpacer(viewModel.announcements, announcement)
            }
            item {
                SpacerBetweenCards()
            }
        }
    }
}

@Composable
fun AnnouncementCard(index: Int, announcement: Announcement, viewModel: AnnouncementViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = DefaultShape
    ) {
        Column(Modifier.padding(16.dp)) {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = announcement.title ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                if (viewModel.group.adminUsername == AppUser.username) {
                    HorizontalSpacer(widthDp = 4)
                    EditDeleteAnnouncementButtonIcons(Icons.Filled.Delete) {
                        viewModel.announcementState.selectedItemIndex = index
                        viewModel.showDeleteAlert = true
                    }
                    HorizontalSpacer(widthDp = 4)
                    EditDeleteAnnouncementButtonIcons(Icons.Filled.Edit) {
                        viewModel.announcementState.selectedItemIndex = index
                        viewModel.announcementState.title = announcement.title!!
                        viewModel.announcementState.description = announcement.description!!
                        viewModel.showEditAlert = true
                    }
                }
            }

            VerticalSpacer(heightDp = 4)
            Text(text = announcement.description!!)
            VerticalSpacer(heightDp = 16)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = announcement.createdBy!!.nickname,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun AlertDialogs(viewModel: AnnouncementViewModel) {
    if (viewModel.showEditAlert) {
        CreateEditAlertDialog(viewModel, "Edit Announcement") {
            viewModel.editAnnouncement()
        }
    }
    DeleteAlertDialog(viewModel)
}

@Composable
private fun DeleteAlertDialog(viewModel: AnnouncementViewModel) {
    if (viewModel.showDeleteAlert) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                Button(onClick = { viewModel.deleteAnnouncement() }) {
                    Text(text = "Ok")
                }
            },
            title = {
                Text(
                    fontWeight = FontWeight.Bold,
                    text = "Delete Announcement",
                    color = MaterialTheme.colorScheme.primary
                )
            }, text = {
                Text(
                    text = "Are you sure you want to delete this announcement",
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            dismissButton = {
                Button(onClick = { viewModel.showDeleteAlert = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}

@Composable
private fun CreateEditAlertDialog(
    viewModel: AnnouncementViewModel,
    titleText: String,
    ok: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(
                fontWeight = FontWeight.Bold,
                text = titleText,
                color = MaterialTheme.colorScheme.onBackground
            )
        }, text = {
            Column {
                OutlinedTextField(
                    placeholder = { Text(text = "Title") },
                    value = viewModel.announcementState.title,
                    onValueChange = {
                        viewModel.announcementState.title = it
                    }
                )
                VerticalSpacer(heightDp = 8)
                OutlinedTextField(minLines = 5, maxLines = 5,
                    placeholder = {
                        Text(text = "Description")
                    }, value = viewModel.announcementState.description,
                    onValueChange = {
                        viewModel.announcementState.description = it
                    }
                )
            }
        }, onDismissRequest = { }, confirmButton = {
            Button(
                onClick = ok
            ) {
                Text(text = "Ok")
            }
        }, dismissButton = {
            Button(onClick = {
                viewModel.showEditAlert = false
                viewModel.showCreateAlert = false
            }) {
                Text(text = "Cancel")
            }
        }
    )
}

@Composable
private fun EditDeleteAnnouncementButtonIcons(imageVector: ImageVector, onClick: () -> Unit) {
    Card(shape = DefaultShape) {
        Icon(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(4.dp),
            imageVector = imageVector,
            contentDescription = null
        )
    }
}
