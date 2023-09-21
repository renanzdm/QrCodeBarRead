package com.renan.qrcodebarreader.domain.di

import com.renan.qrcodebarreader.data.services.ReaderCodeServiceImpl
import com.renan.qrcodebarreader.domain.services.ReaderCodeService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    @Singleton
    abstract fun bindReaderCodeService(
        readerCodeServiceImpl: ReaderCodeServiceImpl
    ): ReaderCodeService

}