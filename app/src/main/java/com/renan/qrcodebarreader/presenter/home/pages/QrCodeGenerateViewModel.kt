package com.renan.qrcodebarreader.presenter.home.pages

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class QrCodeGenerateViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(QrCodeGenerateState())
    val state = _state.asStateFlow()
    
    fun generateImageQrCode(value: String,width:Int,height:Int) {
        val writer = MultiFormatWriter()
        val matrix: BitMatrix = writer.encode(value, BarcodeFormat.QR_CODE, width, height)
        val encoder = BarcodeEncoder()
        _state.update {
            it.copy(qrCode = encoder.createBitmap(matrix))
        }
    }
    
}