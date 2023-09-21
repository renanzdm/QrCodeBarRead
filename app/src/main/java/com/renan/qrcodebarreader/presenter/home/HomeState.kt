package com.renan.qrcodebarreader.presenter.home

data class HomeState(
    val code:String?=null,
    val message:String?=null,
    val openSheet:Boolean = false
)
