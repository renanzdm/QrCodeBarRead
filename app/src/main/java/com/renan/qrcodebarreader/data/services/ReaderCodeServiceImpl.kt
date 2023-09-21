package com.renan.qrcodebarreader.data.services

import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.renan.qrcodebarreader.domain.Resource
import com.renan.qrcodebarreader.domain.services.ReaderCodeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ReaderCodeServiceImpl @Inject constructor() : ReaderCodeService {
    private val options = GmsBarcodeScannerOptions.Builder().enableAutoZoom().build()

    override suspend fun readCodes(context: Context): Resource<Barcode> {
        return try {
            val scanner = GmsBarcodeScanning.getClient(context, options)
            val barcode = withContext(Dispatchers.IO) {
                suspendCancellableCoroutine { continuation ->
                    val onSuccessListener = { barcode: Barcode ->
                        continuation.resume(barcode)
                    }
                    val onFailureListener = { e: Exception ->
                        val errorMessage = e.localizedMessage ?: "Ocorreu um erro ao ler o Código de Barras"
                        continuation.resumeWithException(Exception(errorMessage))
                    }
                    scanner.startScan()
                        .addOnSuccessListener(onSuccessListener)
                        .addOnFailureListener(onFailureListener)
                }
            }
            Resource.Success(data = barcode)
        } catch (e: Exception) {
            val errorMessage = e.localizedMessage ?: "Ocorreu um erro inesperado"
            Resource.Error(message = errorMessage)
        }
    }


}