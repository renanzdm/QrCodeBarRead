package com.renan.qrcodebarreader.presenter.home.pages.docscanning

import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.renan.qrcodebarreader.presenter.components.PhotoPicker

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DocScanningPage(navController: NavController){
    val context = LocalContext.current

    Scaffold (topBar = {
        TopAppBar(title = { Text(text = "Scanner Documento") }, navigationIcon = {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Voltar",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        navController.popBackStack()
                    })
        })
    }){ paddingValues->
        Column {
        PhotoPicker(context =context , onSave ={uri->} )
        }

    }


}



