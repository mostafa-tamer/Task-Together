package com.mostafatamer.tasktogether.presentation.main_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mostafatamer.tasktogether.DefaultShape
import com.mostafatamer.tasktogether.HorizontalPadding
import com.mostafatamer.tasktogether.SpacerBetweenCards
import com.mostafatamer.tasktogether.domain.model.Group
import com.mostafatamer.tasktogether.presentation.components.ActionBar
import com.mostafatamer.tasktogether.presentation.components.FastSwipe
import com.mostafatamer.tasktogether.presentation.components.TrailingVerticalSpacer
import com.mostafatamer.tasktogether.presentation.navigation.MainNavRoutes
import com.mostafatamer.tasktogether.presentation.viewModels.GroupsViewModel


@Composable
fun GroupsScreen(
    viewModel: GroupsViewModel,
    navHostController: NavHostController,
) {

    LaunchedEffect(viewModel) {
        viewModel.getGroups()
    }

    FastSwipe(onRefresh = {
        viewModel.getGroups { it() }
    }) {
        Box(Modifier.fillMaxSize()) {
            Column {
                ActionBar(navHostController)
                LazyList(viewModel, navHostController)
            }
            if (viewModel.showEmptyList) {
                Text(
                    text = "No groups yet",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun ActionBar(
    navHostController: NavHostController,
) {
    ActionBar(
        "Groups",
        prefixComposable = null, suffixComposable = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(
                        onClick = {
                            navHostController.navigate(MainNavRoutes.CreateGroup.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun GroupCard(
    group: Group,
    navHostController: NavHostController,
) {
    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = DefaultShape,
    ) {
        Row(
            Modifier
                .clickable {
                    navHostController.navigate(
                        MainNavRoutes.GroupMainNavHost.passGroup(group)
                    )
                }
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                shape = CircleShape,
//                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary)
            ) {
                Image(
                    painter = rememberImagePainter(group.photo),
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(fontWeight = FontWeight.Bold, fontSize = 20.sp, text = group.name ?: "")
                Text(text = group.description ?: "", maxLines = 4, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}


@Composable
private fun LazyList(viewModel: GroupsViewModel, navHostController: NavHostController) {

    Box(
        Modifier
            .padding(horizontal = HorizontalPadding)
            .fillMaxSize()
    ) {
        Column(Modifier.fillMaxSize()) {
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    SpacerBetweenCards()
                }
                items(
                    viewModel.groups
                ) { item ->
                    GroupCard(item, navHostController)
                    TrailingVerticalSpacer(list = viewModel.groups, item = item)
                }
                item {
                    SpacerBetweenCards()
                }
            }
        }
    }

}

