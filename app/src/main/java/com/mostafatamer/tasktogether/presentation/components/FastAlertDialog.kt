package com.mostafatamer.tasktogether.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState


@Composable
fun FastAlertDialog(
    showAlertDialog: MutableState<Boolean>,
    title: String, text: String = "", body: @Composable() () -> Unit = {},
    negativeText: String = "Cancel", positiveText: String = "Ok",
    onConfirm: () -> Unit,
) {
    if (showAlertDialog.value) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = title)
            },
            text = {
                if (text.isNotBlank()) {
                    Text(text = text)
                }
                body()
            },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                }) {
                    Text(text = positiveText)
                }
            },
            dismissButton = {
                Button(onClick = { showAlertDialog.value = false }) {
                    Text(text = negativeText)
                }
            }
        )
    }
}
