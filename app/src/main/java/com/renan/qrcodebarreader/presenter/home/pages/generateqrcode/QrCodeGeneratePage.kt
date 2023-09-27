package com.renan.qrcodebarreader.presenter.home.pages.generateqrcode

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.renan.qrcodebarreader.R
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Objects


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeGeneratePage(qrCodeGenerateViewModel: QrCodeGenerateViewModel = hiltViewModel(), navController: NavController) {
    val textQrCode = remember { mutableStateOf("") }
    val uiState = qrCodeGenerateViewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    BoxWithConstraints {
        val sizes = this
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .imePadding()
                .padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(title = { Text(text = "Gerar QrCode") }, navigationIcon = {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Voltar",
                    modifier = Modifier.padding(end = 16.dp)
                        .clickable {
                        navController.popBackStack()
                    }
                )
            })
            if (uiState.value.qrCode == null) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_qr_code_scanner_24),
                    contentDescription = "QRCode",
                    modifier = Modifier.size(
                        height = (sizes.maxHeight.value * 0.6).dp, width = sizes.maxWidth
                    )
                )
            } else {
                Column (horizontalAlignment = Alignment.End){
                    IconButton(onClick = { shareImage(uiState.value.qrCode!!, context = context)}) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Compartilhar",
                        )
                    }
                    Image(
                        bitmap = uiState.value.qrCode!!.asImageBitmap(),
                        contentDescription = "QrCode",
                        modifier = Modifier.size(
                            height = (sizes.maxHeight.value * 0.6).dp, width = sizes.maxWidth
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
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
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val timeStamp = LocalDateTime.now().format(formatter)
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, ".jpg", externalCacheDir
    )
}

private fun getmageToShare(bitmap: Bitmap, context: Context): Uri? {
    try {
        val file = context.createImageFile()
        val uri = FileProvider.getUriForFile(
            Objects.requireNonNull(context), context.packageName + ".provider", file
        );
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
        outputStream.flush()
        outputStream.close()
        return uri
    } catch (e: Exception) {
        Toast.makeText(context, "" + e.message, Toast.LENGTH_LONG).show()
    }
    return null
}

private fun shareImage(bitmap: Bitmap, context: Context) {
    val uri = getmageToShare(bitmap, context)
    val intent = Intent(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.putExtra(Intent.EXTRA_TEXT, "Meu QrCode")
    intent.type = "image/png"
    context.startActivity(Intent.createChooser(intent, "Share Via"))
}