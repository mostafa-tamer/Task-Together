package com.mostafatamer.tasktogether.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mostafatamer.tasktogether.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ImagePicker(
    defaultImage: Painter? = null,
    onImageSelected: (Uri) -> Unit,
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()

    ) { uri: Uri? ->
        uri?.let {
            onImageSelected(uri)
            imageUri = it
        }
    }

    Surface(shape = CircleShape, modifier = Modifier.size(128.dp)) {
        Image(
            painter =
            if (imageUri != null) rememberImagePainter(imageUri)
            else {
                defaultImage ?: painterResource(R.drawable.photo_svgrepo_com)
            },
            contentDescription = null,
            modifier = Modifier
                .size(256.dp)
                .clickable {
                    launcher.launch("image/*")
                },
            colorFilter = if (imageUri == null && defaultImage == null) ColorFilter.tint(
                MaterialTheme.colorScheme.primary
            ) else null
        )
    }
}