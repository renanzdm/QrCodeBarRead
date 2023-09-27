package com.renan.qrcodebarreader.presenter.home.pages.generateqrcode

import android.graphics.Bitmap

data class QrCodeGenerateState(
    var isLoading:Boolean = false,
    var qrCode:Bitmap? = null
)
