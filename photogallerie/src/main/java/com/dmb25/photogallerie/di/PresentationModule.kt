package com.dmb25.photogallerie.di

import com.dmb25.photogallerie.presentation.GaleryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::GaleryViewModel)
}