package com.renan.qrcodebarreader.presenter.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(backOnTap: (() -> Unit)?, title: String) {
    TopAppBar(title = { Text(text = title) }, navigationIcon = {
        Icon(
            Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = "Voltar",
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable {
                    backOnTap?.invoke()
                })
    })
}