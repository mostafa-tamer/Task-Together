package com.mostafatamer.tasktogether.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mostafatamer.tasktogether.R
import com.mostafatamer.tasktogether.presentation.components.ActionBar
import com.mostafatamer.tasktogether.presentation.components.NavigateUp
import com.mostafatamer.tasktogether.presentation.components.TrailingVerticalSpacer
import com.mostafatamer.tasktogether.presentation.components.UserSelectionCard
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.AddMembersToProjectViewModel

@Composable
fun AddProjectMembersScreen(
    viewModel: AddMembersToProjectViewModel,
    navHostController: NavHostController,
) {

    LaunchedEffect(Unit) {
        viewModel.getTopicCandidates()
    }

    if (viewModel.membersAdded) {
        viewModel.membersAdded = false
        Toast.makeText(LocalContext.current, "Members Added", Toast.LENGTH_SHORT).show()
        navHostController.navigateUp()
    }

    Scaffold(
        topBar = {
            ActionBar(
                label = "Add Members",
                prefixComposable = { NavigateUp(navHostController = navHostController) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.addMembersToTopic(viewModel.project) }) {
                Icon(
                    painter = painterResource(id = R.drawable.group_add_svgrepo_com),
                    contentDescription = null
                    ,Modifier.size(24.dp)
                )
            }
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (viewModel.showEmptyList) {
                Text(
                    text = "No members yet",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(modifier = Modifier.padding(8.dp)) {
                LazyColumn {
                    itemsIndexed(viewModel.members) { index, user ->
                        UserSelectionCard(userSelection = user) {
                            viewModel.toggleUserSelection(index)
                        }
                        TrailingVerticalSpacer(list = viewModel.members, item = user)
                    }
                }
            }
        }
    }
}