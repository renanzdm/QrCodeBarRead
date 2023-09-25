@file:OptIn(ExperimentalFoundationApi::class)

package com.renan.qrcodebarreader.presenter.home.pages

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.renan.qrcodebarreader.R
import java.io.File
import java.time.LocalDate


@Composable
fun QrCodeGeneratePage(qrCodeGenerateViewModel: QrCodeGenerateViewModel = hiltViewModel()) {
    val textQrCode = remember { mutableStateOf("") }
    val uiState = qrCodeGenerateViewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    BoxWithConstraints {
        val sizes = this
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .imePadding()
                .padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Voltar")
            if (uiState.value.qrCode == null) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_qr_code_scanner_24),
                    contentDescription = "QRCode",
                    modifier = Modifier.size(
                        height = (sizes.maxHeight.value * 0.7).dp, width = sizes.maxWidth
                    )
                )
            } else {
                Image(
                    bitmap = uiState.value.qrCode!!.asImageBitmap(),
                    contentDescription = "QrCode",
                    modifier = Modifier.size(
                        height = (sizes.maxHeight.value * 0.7).dp, width = sizes.maxWidth
                    )
                )
            }
            OutlinedTextField(
                value = textQrCode.value, onValueChange = {
                    textQrCode.value = it
                }, modifier = Modifier.fillMaxWidth()
            )
            AnimatedVisibility(
                visible = textQrCode.value.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(onClick = {
                    qrCodeGenerateViewModel.generateImageQrCode(
                        textQrCode.value,
                        height = (sizes.maxHeight.value * 0.7).toInt(),
                        width = sizes.maxWidth.value.toInt()
                    )
                }) {
                    Text(text = "Gerar QrCode")
                }
            }
            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}


fun Context.createImageFile(): File {
    val date = LocalDate.now()
    val text = date.format(formatter)
    val parsedDate = LocalDate.parse(text, formatter)
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, ".jpg", externalCacheDir
    )
}

