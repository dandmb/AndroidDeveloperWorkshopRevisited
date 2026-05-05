package com.dmb25.photogallerie.data.repo

import com.dmb25.photogallerie.data.model.MyGallery
import kotlinx.coroutines.flow.Flow

interface GalleryRepo {
    suspend fun getGallery(): Flow<List<MyGallery>>
    suspend fun getGalleryById(id: Int): MyGallery?
}
