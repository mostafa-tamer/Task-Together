package com.mostafatamer.tasktogether.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mostafatamer.tasktogether.DefaultShape
import com.mostafatamer.tasktogether.domain.model.Group
import com.mostafatamer.tasktogether.presentation.components.ActionBar
import com.mostafatamer.tasktogether.presentation.components.NavigateUp
import com.mostafatamer.tasktogether.presentation.components.Search
import com.mostafatamer.tasktogether.presentation.components.TrailingVerticalSpacer
import com.mostafatamer.tasktogether.presentation.viewModels.JoinGroupViewModel


@Composable
fun JoinGroupScreen(
    viewModel: JoinGroupViewModel,
    navHostController: NavHostController,
) {

    BackHandler {
        if (viewModel.showSearchBar) {
            viewModel.showSearchBar = false
            viewModel.getAllAvailableGroups()
        } else {
            navHostController.navigateUp()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getAllAvailableGroups()
    }

    if (viewModel.groupToJoin != null) {
        navHostController.navigateUp()
    }

    Column {
        ActionBar(navHostController, viewModel)

        LazyColumn(Modifier.padding(16.dp)) {
            items(viewModel.groups) { group ->
                GroupCard(group, viewModel)
                TrailingVerticalSpacer(viewModel.groups, group)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun GroupCard(
    group: Group,
    viewModel: JoinGroupViewModel,
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                viewModel.joinGroup(group)
            },
        shape = DefaultShape,
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(Modifier.padding(12.dp)) {
            Card(shape = DefaultShape) {
                Image(
                    painter = rememberImagePainter(group.photo),
                    contentDescription = null,
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                        .background(Color.Gray),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = group.name ?: "")
                Text(text = group.description ?: "")
            }
        }
    }
}

@Composable
private fun ActionBar(
    navHostController: NavHostController,
    viewModel: JoinGroupViewModel,
) {
    val prefixComposable = @Composable {
        NavigateUp(navHostController = navHostController)
    }
    val suffixComposable = @Composable {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            SearchIcon(viewModel)
        }
    }
    val overlay = @Composable {
        AnimatedVisibility(
            visible = viewModel.showSearchBar,
            enter = fadeIn(),
        ) {
            Search(
                Modifier.fillMaxWidth(),
                onSearch = { search -> viewModel.getGroupsToJoin(search) },
            )
        }
    }

    ActionBar(
        "Join Group",
        prefixComposable = prefixComposable,
        suffixComposable = { suffixComposable() },
        overlay
    )
}


@Composable
private fun SearchIcon(
    viewModel: JoinGroupViewModel,
) {
    IconButton(
        onClick = {
            viewModel.showSearchBar = true
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null
        )
    }
}

