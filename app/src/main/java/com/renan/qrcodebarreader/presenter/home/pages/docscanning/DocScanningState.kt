package com.renan.qrcodebarreader.presenter.home.pages.docscanning

import android.graphics.Bitmap

data class DocScanningState(
    var loading :Boolean = false,
    var image:Bitmap? = null,
    var errorMessage:String?=null
)
