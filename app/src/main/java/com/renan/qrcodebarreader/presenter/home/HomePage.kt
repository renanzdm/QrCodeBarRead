package com.renan.qrcodebarreader.presenter.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.renan.qrcodebarreader.R
import com.renan.qrcodebarreader.presenter.theme.md_theme_dark_onTertiary
import com.renan.qrcodebarreader.presenter.theme.md_theme_dark_onTertiaryContainer
import com.renan.qrcodebarreader.presenter.theme.md_theme_dark_secondary
import com.renan.qrcodebarreader.presenter.theme.md_theme_dark_tertiary
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(homeViewModel: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState()
    val uiState = homeViewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val listButtons: List<ButtonsModel> = listOf(
        ButtonsModel(name = "Ler Codigo/QrCode",
            icon = R.drawable.baseline_document_scanner_24,
            onTap = { coroutineScope.launch { homeViewModel.readQrCode(context = context) } }),
        ButtonsModel(name = "Gerar QrCode", icon = R.drawable.baseline_qr_code_scanner_24),
        ButtonsModel(name = "Aprimorar documento", icon = R.drawable.baseline_qr_code_scanner_24),
    )


    Scaffold { _ ->
        // Sheet content
        if (uiState.value.openSheet) {


            ModalBottomSheet(
                onDismissRequest = { homeViewModel.closeBottomSheet() },
                sheetState = bottomSheetState,
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(
                        // Note: If you provide logic outside of onDismissRequest to remove the sheet,
                        // you must additionally handle intended state cleanup, if any.
                        onClick = {
                            coroutineScope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    homeViewModel.closeBottomSheet()
                                }
                            }
                        }
                    ) {
                        Text("Hide Bottom Sheet")
                    }
                }
                var text by remember { mutableStateOf("") }
                OutlinedTextField(value = text, onValueChange = { text = it })
                LazyColumn {
                    items(50) {
                        ListItem(
                            headlineContent = { Text("Item $it") },
                            leadingContent = {
                                Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = "Localized description"
                                )
                            }
                        )
                    }
                }
            }
        }
    }


        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Leitor e Digitalizador de Documentos", style = TextStyle(
                    fontSize = 25.sp, fontWeight = FontWeight.ExtraBold
                )
            )

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
            ) {
                items(listButtons) { model ->
                    Column(
                        modifier = Modifier
                            .height(130.dp)
                            .padding(20.dp)
                            .shadow(elevation = 4.dp, spotColor = Color.Gray)
                            .clip(RoundedCornerShape(22f))
                            .background(md_theme_dark_onTertiary)
                            .clickable { model.onTap?.invoke() },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = model.name, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(8.dp))
                        Icon(
                            painter = painterResource(id = model.icon), contentDescription = "Icon"
                        )
                    }
                }
            }

        }

}

data class ButtonsModel(
    var name: String, var icon: Int, var onTap: (() -> Unit)? = null
)

