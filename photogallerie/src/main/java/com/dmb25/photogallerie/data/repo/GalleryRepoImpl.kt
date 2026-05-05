package com.dmb25.photogallerie.data.repo

import com.dmb25.photogallerie.data.local.DataSource
import com.dmb25.photogallerie.data.model.MyGallery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GalleryRepoImpl(
    private val dataSource: DataSource
) : GalleryRepo  {
    override suspend fun getGallery(): Flow<List<MyGallery>> = flow{
        delay(3000)
        emit(DataSource.galleryList)
    }.flowOn(Dispatchers.IO)

    override suspend fun getGalleryById(id: Int): MyGallery? {
        val item = dataSource.galleryList.find { it.id == id }
        return item
    }


}