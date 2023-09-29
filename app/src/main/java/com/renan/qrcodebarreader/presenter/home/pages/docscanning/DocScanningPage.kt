package com.renan.qrcodebarreader.presenter.home.pages.docscanning

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.renan.qrcodebarreader.presenter.components.AppTopBar
import com.renan.qrcodebarreader.presenter.components.PhotoPicker

@Composable
fun DocScanningPage(navController: NavController, viewModel: DocScanningViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val uiState = viewModel.state.collectAsStateWithLifecycle()
    Scaffold(topBar = {
        AppTopBar(
            backOnTap = { navController.popBackStack() }, title = "Digitalizar Documento"
        )
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            PhotoPicker(
                context = context,
                onSave = { uri -> viewModel.detectDocument(context = context, uri = uri)},
                )
            if(uiState.value.image!=null) {
                Image(bitmap = uiState.value.image!!.asImageBitmap(), contentDescription = "")
            }
        }

    }


}



