package com.mostafatamer.tasktogether.presentation

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mostafatamer.tasktogether.DefaultShape
import com.mostafatamer.tasktogether.presentation.components.ActionBar
import com.mostafatamer.tasktogether.presentation.components.ImagePicker
import com.mostafatamer.tasktogether.presentation.components.NavigateUp
import com.mostafatamer.tasktogether.presentation.components.VerticalSpacer
import com.mostafatamer.tasktogether.presentation.viewModels.CreateGroupViewModel
import com.mostafatamer.tasktogether.showToast


@Composable
fun CreateGroupScreen(
    createGroupViewModel: CreateGroupViewModel,
    navHostController: NavHostController,
) {
    val context = LocalContext.current

    var groupName: String by remember { mutableStateOf("") }
    var groupDescription: String by remember { mutableStateOf("") }


    Column(Modifier.fillMaxSize()) {
        ActionBar("Create Group", prefixComposable = {
            NavigateUp(navHostController = navHostController)
        })

        Column(
            Modifier
                .padding(top = 32.dp)
                .padding(horizontal = 8.dp)
        ) {

            var imageUri: Uri? by remember { mutableStateOf(null) }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                ImagePicker {
                    imageUri = it
                }
            }



            VerticalSpacer(heightDp = 16)


            OutlinedTextField(
                shape = DefaultShape,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
                minLines = 3, maxLines = 3,
                placeholder = { Text(text = "Group Name") },
                value = groupName,
                onValueChange = { groupName = it }
            )


            VerticalSpacer(heightDp = 16)


            OutlinedTextField(
                shape = DefaultShape,
                singleLine = false,
                minLines = 4,
                maxLines = 4,
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text(text = "Group Description (Optional)") },
                value = groupDescription,
                onValueChange = { groupDescription = it }
            )

            VerticalSpacer(heightDp = 16)

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End),
                onClick = {
                    if (groupName.isNotBlank()) {
                        createGroupViewModel.createGroup(groupName, groupDescription, imageUri) {
                            if (it) {
                                showToast(context, "Group created successfully")
                                navHostController.navigateUp()
                            } else {
                                showToast(context, "Failed to create group")
                            }
                        }
                    } else {
                        showToast(context, "Group name should not be empty")
                    }
                }
            ) {
                Text(text = "Create")
            }
        }
    }
}