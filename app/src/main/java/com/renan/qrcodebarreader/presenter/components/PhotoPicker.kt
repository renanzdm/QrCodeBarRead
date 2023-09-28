package com.renan.qrcodebarreader.presenter.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.renan.qrcodebarreader.R
import com.renan.qrcodebarreader.presenter.home.pages.generateqrcode.createImageFile
import com.renan.qrcodebarreader.presenter.theme.md_theme_dark_inverseOnSurface
import com.renan.qrcodebarreader.presenter.theme.md_theme_dark_primary
import com.renan.qrcodebarreader.presenter.theme.md_theme_light_inverseOnSurface
import com.renan.qrcodebarreader.presenter.theme.md_theme_light_primary
import java.util.Objects

@Composable
fun PhotoPicker(context: Context, onSave: (Uri) -> Unit, image: Uri? =null) {
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context), context.packageName + ".provider", file
    );
    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        capturedImageUri = uri
        onSave.invoke(capturedImageUri)
    }
    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
            onResult = {})
    Box(modifier = Modifier
        .height(150.dp)
        .width(150.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(if (isSystemInDarkTheme()) md_theme_dark_primary else md_theme_light_primary,)
        .clickable {
            val permissionCheckResult =
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(uri)
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }) {

        Column(modifier = Modifier.align(Alignment.Center)) {
            if (image != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = image),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                    contentDescription = null,
                    tint = if (isSystemInDarkTheme()) md_theme_dark_inverseOnSurface else md_theme_light_inverseOnSurface,
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)
                )
            }
        }
    }
}