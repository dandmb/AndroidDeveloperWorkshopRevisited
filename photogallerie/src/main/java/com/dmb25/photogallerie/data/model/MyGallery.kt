package com.dmb25.photogallerie.data.model

import androidx.annotation.DrawableRes

data class MyGallery(
    val id: Int,
    val title: String,
    val place: String,
    val year: String,
    @param:DrawableRes val image: Int
)