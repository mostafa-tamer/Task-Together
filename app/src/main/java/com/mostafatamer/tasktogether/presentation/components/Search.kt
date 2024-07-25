package com.mostafatamer.tasktogether.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import com.mostafatamer.tasktogether.DefaultShape


@Composable
fun Search(
    modifier: Modifier,
    onSearch: (String) -> Unit,
) {
    val (searchText: String, setSearchText: (String) -> Unit) = remember { mutableStateOf("") }
    val focusRequester: FocusRequester by remember { mutableStateOf(FocusRequester()) }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Card(shape = DefaultShape, modifier = modifier) {
        TextField(
            value = searchText,

            onValueChange = { text ->
                setSearchText(text)
                onSearch(text)
            },
            modifier = Modifier
                .background( MaterialTheme.colorScheme.surfaceContainerHighest)
                .fillMaxWidth()
                .focusRequester(focusRequester),
            placeholder = {
                Text(text = "Search")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(searchText)
                },
            ),
            textStyle = TextStyle(fontSize = 16.sp),
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    ClearButton {
                        setSearchText("")
                        onSearch("")
                    }
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White
            )
        )
    }
}

@Composable
fun ClearButton(onClear: () -> Unit) {
    IconButton(onClick = { onClear() }) {
        Icon(Icons.Filled.Clear, contentDescription = "Clear")
    }
}