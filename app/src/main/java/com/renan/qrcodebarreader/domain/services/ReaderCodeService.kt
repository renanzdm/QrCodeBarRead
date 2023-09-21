package com.renan.qrcodebarreader.domain.services

import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.renan.qrcodebarreader.domain.Resource

interface ReaderCodeService  {

suspend fun readCodes(context: Context): Resource<Barcode>

}