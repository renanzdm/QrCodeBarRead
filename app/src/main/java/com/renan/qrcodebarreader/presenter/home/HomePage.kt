package com.renan.qrcodebarreader.presenter.home

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.sharp.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.renan.qrcodebarreader.R
import com.renan.qrcodebarreader.presenter.AppRoutes
import com.renan.qrcodebarreader.presenter.theme.md_theme_dark_inverseOnSurface
import com.renan.qrcodebarreader.presenter.theme.md_theme_dark_primary
import com.renan.qrcodebarreader.presenter.theme.md_theme_light_inverseOnSurface
import com.renan.qrcodebarreader.presenter.theme.md_theme_light_primary
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePage(homeViewModel: HomeViewModel = hiltViewModel(), navController: NavHostController) {
    val context = LocalContext.current
    val uiState = homeViewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val listButtons: List<ButtonsModel> = listOf(
        ButtonsModel(name = "Ler Codigo de Barras/QrCode",
            icon = R.drawable.baseline_document_scanner_24,
            onTap = { coroutineScope.launch { homeViewModel.readQrCode(context = context) } }),
        ButtonsModel(name = "Gerar QrCode", icon = R.drawable.baseline_qr_code_scanner_24, onTap = {
            navController.navigate(AppRoutes.generateQrCodeRoute)
        }),
        ButtonsModel(name = "Copiar Texto de Documento",
            icon = R.drawable.baseline_list_24,
            onTap = { navController.navigate(AppRoutes.docToText) }),
        ButtonsModel(name = "Digitalizar Documento",
            icon = R.drawable.baseline_adf_scanner_24,
            onTap = { navController.navigate(AppRoutes.docScanning) }),
    )

    Scaffold { _ ->
        if (uiState.value.openSheet) {
            BottomSheetCodeValue(
                onClose = { homeViewModel.closeBottomSheet() }, data = uiState.value.code
            )
        }
    }
    Column(
        Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
        ) {
            items(listButtons) { model ->
                Column(
                    modifier = Modifier
                        .height(200.dp)
                        .width(120.dp)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(22f))
                        .background(if (isSystemInDarkTheme()) md_theme_dark_primary else md_theme_light_primary)
                        .clickable { model.onTap?.invoke() },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = model.name, textAlign = TextAlign.Center, style = TextStyle(
                            color = if (isSystemInDarkTheme()) md_theme_dark_inverseOnSurface else md_theme_light_inverseOnSurface,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(
                        painter = painterResource(id = model.icon),
                        contentDescription = "Icon",
                        tint = if (isSystemInDarkTheme()) md_theme_dark_inverseOnSurface else md_theme_light_inverseOnSurface
                    )
                }
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetCodeValue(onClose: () -> Unit, data: String?) {
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { onClose.invoke() },
        sheetState = bottomSheetState,
    ) {
        BoxWithConstraints(modifier = Modifier.padding(22.dp)) {
            val sizes = this
            Row(modifier = Modifier.align(Alignment.TopStart)) {
                Text(
                    text = data.orEmpty(),
                    modifier = Modifier
                        .width((sizes.maxWidth.value * 0.9).dp)
                        .align(Alignment.CenterVertically),
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Light)
                )
                IconButton(onClick = {
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                    val clip: ClipData = ClipData.newPlainText("Valor Copiado", data)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "Copiado!!", Toast.LENGTH_LONG).show()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_content_copy_24),
                        contentDescription = "Copiar",
                        tint = Color.Blue
                    )
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}


data class ButtonsModel(
    var name: String, var icon: Int, var onTap: (() -> Unit)? = null
)

