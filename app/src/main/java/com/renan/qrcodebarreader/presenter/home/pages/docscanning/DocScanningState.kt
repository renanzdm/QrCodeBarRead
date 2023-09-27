package com.renan.qrcodebarreader.presenter.home.pages.docscanning

import android.net.Uri

data class DocScanningState(
    var loading: Boolean = false,
    var imageUri: Uri? = null,
    var text: String? = null,
    var errorMessage:String? = null
)
