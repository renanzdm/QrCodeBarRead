package com.renan.qrcodebarreader.presenter.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renan.qrcodebarreader.domain.Resource
import com.renan.qrcodebarreader.domain.services.ReaderCodeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val readerCodeService: ReaderCodeService) :
    ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    var state = _state.asStateFlow()


    fun closeBottomSheet(){
        _state.update {
            it.copy(openSheet = false)
        }
    }

  suspend  fun readQrCode(context: Context) {
      viewModelScope.launch {
          when (val response = readerCodeService.readCodes(context = context)) {
              is Resource.Success -> {
                  _state.update {
                      it.copy(code = response.data!!.displayValue, openSheet = true)
                  }
              println(response.data!!)
              }

              is Resource.Error -> {
                  _state.update {
                      it.copy(message = response.message)
                  }
              }
          }
      }
    }
}