package com.dmb25.photogallerie.di

import com.dmb25.photogallerie.data.local.DataSource
import com.dmb25.photogallerie.data.repo.GalleryRepo
import com.dmb25.photogallerie.data.repo.GalleryRepoImpl
import org.koin.dsl.module


val dataModule = module {
    single { DataSource }
    single<GalleryRepo> { GalleryRepoImpl(get()) }
}