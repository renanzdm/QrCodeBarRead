package com.renan.qrcodebarreader.presenter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.renan.qrcodebarreader.presenter.theme.md_theme_dark_inverseOnSurface
import com.renan.qrcodebarreader.presenter.theme.md_theme_light_inverseOnSurface


@Composable
fun CustomSnackBarHost(
    hostState: SnackbarHostState,
    color: Color = Color.DarkGray
) {
    SnackbarHost(
        hostState = hostState,
        snackbar = { data: SnackbarData ->
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .wrapContentSize()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color)
                        .padding(22.dp),
                    text = data.visuals.message,
                    color = if (isSystemInDarkTheme()) md_theme_dark_inverseOnSurface else md_theme_light_inverseOnSurface
                )
            }
        }
    )

}
