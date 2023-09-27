package com.renan.qrcodebarreader.presenter.home.pages.docscanning

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class DocScanningViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(DocScanningState())
    var state = _state.asStateFlow()
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun scanningDocument(context: Context) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(loading = true) }
                val textRecognition: Text = withContext(Dispatchers.IO) {
                    val inputImage = InputImage.fromFilePath(context, _state.value.imageUri!!)
                    suspendCancellableCoroutine { continuation ->
                        val onSuccessListener = { text: Text -> continuation.resume(text) }
                        val onFailureListener =
                            { ex: Exception -> continuation.resumeWithException(ex) }
                        textRecognizer.process(inputImage)
                            .addOnSuccessListener(onSuccessListener)
                            .addOnFailureListener(onFailureListener)
                            .addOnCanceledListener { continuation.cancel() }
                    }
                }
                _state.update { it.copy(loading = false, text = textRecognition.text) }
            } catch (e: Exception) {
                _state.update { it.copy(loading = false, errorMessage = e.localizedMessage, imageUri = null) }
            }
        }
    }

    fun saveUrlImage(uri: Uri) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    imageUri = uri
                )
            }
        }
    }
}