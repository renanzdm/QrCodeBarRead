package com.renan.qrcodebarreader.presenter.home.pages.docToText

import android.net.Uri

data class DocToTextState(
    var loading: Boolean = false,
    var imageUri: Uri? = null,
    var text: String? = null,
    var errorMessage:String? = null
)
