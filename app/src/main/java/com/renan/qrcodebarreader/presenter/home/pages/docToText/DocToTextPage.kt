package com.renan.qrcodebarreader.presenter.home.pages.docToText

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.renan.qrcodebarreader.R
import com.renan.qrcodebarreader.presenter.components.AppTopBar
import com.renan.qrcodebarreader.presenter.components.CustomSnackBarHost
import com.renan.qrcodebarreader.presenter.components.PhotoPicker
import com.renan.qrcodebarreader.presenter.home.pages.generateqrcode.createImageFile
import com.renan.qrcodebarreader.presenter.theme.md_theme_dark_error
import com.renan.qrcodebarreader.presenter.theme.md_theme_dark_inverseOnSurface
import com.renan.qrcodebarreader.presenter.theme.md_theme_dark_primary
import com.renan.qrcodebarreader.presenter.theme.md_theme_light_inverseOnSurface
import com.renan.qrcodebarreader.presenter.theme.md_theme_light_primary
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DocToTextPage(
    navController: NavController, viewModel: DocToTextViewModel = hiltViewModel()
) {
    val uiState = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val snackHost = remember { SnackbarHostState() }
    LaunchedEffect(key1 = uiState.value.errorMessage) {
        if (uiState.value.errorMessage != null) {
            snackHost.showSnackbar(message = uiState.value.errorMessage!!)
        }
    }
    Scaffold(
        topBar = { AppTopBar(backOnTap = {navController.popBackStack()}, title = "Copiar texto de Documentos") },
        snackbarHost = {
            CustomSnackBarHost(
                hostState = snackHost,
                color = md_theme_dark_error
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.value.loading) {
                CircularProgressIndicator()
            } else {
                PhotoPicker(
                    context, onSave = { uri ->
                        viewModel.saveUrlImage(
                            uri = uri
                        )
                        viewModel.scanningDocument(context = context)
                    }, image = uiState.value.imageUri
                )
                AnimatedVisibility(visible = uiState.value.text != null) {
                    DocTextScanned(text = uiState.value.text!!)
                }
            }
        }
    }
}

@Composable
fun DocTextScanned(text: String) {
    val context = LocalContext.current
    var textValue by remember { mutableStateOf(text) }
    Column(modifier = Modifier.padding(16.dp)) {
        IconButton(onClick = {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Texto Copiado", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Copiado!!", Toast.LENGTH_LONG).show()

        }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_content_copy_24),
                contentDescription = "Copiar",
                tint = Color.Blue
            )
        }
        OutlinedTextField(value = textValue, onValueChange = {
            textValue = it
        }, modifier = Modifier.fillMaxSize())
    }

}


